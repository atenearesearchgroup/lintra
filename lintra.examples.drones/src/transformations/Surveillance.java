package transformations;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import blackboard.*;
import surveillance.Clock;
import surveillance.Coordinate;
import surveillance.Drone;
import surveillance.GunShot;
import surveillance.MovingObject;
import surveillance.UnidentifiedObject;
import transfo.*;
import uncertaintypes.UBoolean;
import uncertaintypes.UInteger;
import uncertaintypes.UReal;

public class Surveillance implements ITransformation {

	private static final UReal SECS = new UReal(1,0);
	
	private IArea srcArea, trgArea, currentIdArea, idCorrespondencesArea, deletesArea;
	
	public Surveillance(IArea srcArea, IArea trgArea, IArea currentIdArea, IArea correspondencesArea, IArea deletesArea) {
		this.srcArea = srcArea;
		this.trgArea = trgArea;
		this.currentIdArea = currentIdArea;
		this.idCorrespondencesArea = correspondencesArea;
		this.deletesArea = deletesArea;
	}

	@Override
	public void transform(Collection<blackboard.IdentifiableElement> objs,
			IMaster masterNextTransfo) throws blackboard.BlackboardException,
			InterruptedException {
		
		/**
		 * The ATL refining execution mode transforms the elements identified by the source patterns according to the behaviour
		 * defined in the rules. Those model elements that are not explicitly affected by the rules (either directly or indirectly)
		 * remain unchanged.
		 * 
		 * Two options: 
		 * 
		 * 1) Write in the output model the elements that has been altered by the transfo and then copy the rest of the elements
		 *  
		 * 2) The target model is a copy of the source model, the source model is navigated but the updates are made in the target
		 * model.
		 * 
		 * Should we follow the second approach in LinTra? The elements have the same identifiers in both models so, although we
		 * the source model is navigated and the target updated, we know the correspondences).
		 * The advantage of this second approach is that we don't have to keep track to the elements that haven't been altered.
		 * -> If we follow this approach, how to deal with the creation of new elements?
		 * -> The deletion of elements leaves gaps in the identifiers of the output model preventing the application of MT chains.
		 *  
		 */
		
		LinkedList<IdentifiableElement> createdElems = new LinkedList<IdentifiableElement>();
		LinkedList<IdentifiableElement> modifiedElems = new LinkedList<IdentifiableElement>();
		LinkedList<IdentifiableElement> deletedElems = new LinkedList<IdentifiableElement>();
		
		List<IdentifiableElement> idCorrespondances = new LinkedList<IdentifiableElement>();
		
		for (IdentifiableElement ie : objs){
			if (ie instanceof UnidentifiedObject){
				UnidentifiedObject u = (UnidentifiedObject) ie;
				UBoolean speedGreater30 = u.getSpeed().gt(new UReal(30, 0));
				if (u.getConfidence()>0.65 &&
						speedGreater30.getC()>0.5 &&
						selectByConfidence(u.getShotIDs(), 0.95).size()==0){
					
					List<Drone> drones = readDrones();
					for (Drone d : drones){
						String cDroneID = d.getPositionID();
						Coordinate cDrone = (Coordinate) srcArea.read(cDroneID);
						String cUID = u.getPositionID();
						Coordinate cUO = (Coordinate) srcArea.read(cUID);
						UBoolean distanceLower1000 = cDrone.distance(cUO).lt(new UReal(1000,0)); 
						if (distanceLower1000.getC()>0.5){
							
							Coordinate cgs = new Coordinate();
							cgs.setId(TraceFunction.create(d.getId(), 1, "DRONES"));
							cgs.setX(cDrone.getX());
							cgs.setY(cDrone.getY());
							
							GunShot gs = new GunShot();
							gs.setId(TraceFunction.create(d.getId(), 2, "DRONES"));
							gs.setPositionID(cgs.getId());
							gs.setDroneID(d.getId());
							gs.setTargetID(u.getId());
							double matchingConfidence = u.getConfidence()*0.99*
									speedGreater30.and(distanceLower1000).getC();
							gs.setConfidence(matchingConfidence);
							gs.updateAngle(cDrone, cUO);
							gs.updateHitsTarget(cDrone, cUO, u.getWidth());
							
							createdElems.add(cgs);
							createdElems.add(gs);
							
						}
					}
				}
			}
			if (ie instanceof MovingObject){
				MovingObject mo = (MovingObject) ie;
				Coordinate moC = (Coordinate) srcArea.read(((MovingObject) ie).getPositionID());
				
				UReal d = mo.getSpeed().mult(SECS);
				moC.setX(moC.getX().add(d.mult(mo.getAngle().cos())));
				moC.setY(moC.getY().add(d.mult(mo.getAngle().sin())));
				
				modifiedElems.add(mo);
				modifiedElems.add(moC);
				
			}
			if (ie instanceof Coordinate){
//				deletedElems.add(ie);
			}
			if (ie instanceof Clock){
				((Clock) ie).setNow(((Clock) ie).getNow().add(new UInteger(1,0)));
				modifiedElems.add(ie);
			}
		}
		
		Range rangeOfIds = requestRangeOfIds(createdElems.size());
		for (IdentifiableElement ie : createdElems){
			String id = Double.toString(rangeOfIds.getCurrent());
			ie.setId(id);
			idCorrespondances.add(new IdCorrespondence(ie.getTrgId(), id));
			rangeOfIds.next();
		}
		idCorrespondencesArea.writeAll(idCorrespondances);
		trgArea.writeAll(createdElems);
		trgArea.writeAll(modifiedElems);
		trgArea.takeAll(ids(deletedElems));
		deletesArea.writeAll(deletedElems);
		
	}
	
	private List<Drone> readDrones() {
		try {
			List<Drone> drones = new LinkedList<Drone>();
			List<IdentifiableElement> elems = (List<IdentifiableElement>) srcArea.read(srcArea.size());
			for (IdentifiableElement e : elems){
				if (e instanceof Drone){
					drones.add((Drone) e);
				}
			}
			return drones;
		} catch (BlackboardException e) {
			e.printStackTrace();
		}
		return new LinkedList<Drone>();
	}

	private List<GunShot> selectByConfidence(List<String> shotIDs, double c) {
		try {
			List<GunShot> gunShots = new LinkedList<GunShot>();
			List<IdentifiableElement> elems = (List<IdentifiableElement>) srcArea.read(srcArea.size());
			for (IdentifiableElement e : elems){
				if (e instanceof GunShot && ((GunShot) e).getConfidence()>c){
					gunShots.add((GunShot) e);
				}
			}
			return gunShots;
		} catch (BlackboardException e) {
			e.printStackTrace();
		}
		return new LinkedList<GunShot>();
	}

	private Collection<String> ids(LinkedList<IdentifiableElement> deletedElems) {
		LinkedList<String> ids = new LinkedList<String>();
		for (IdentifiableElement ie : deletedElems){
			ids.add(ie.getId());
		}
		return ids;
	}

	private Range requestRangeOfIds(int numberOfIds) throws BlackboardException, InterruptedException {
		CurrentId cid = (CurrentId) currentIdArea.take(LinTraParameters.CURRENT_AREA_ID); 
		while (cid == null){
			cid = (CurrentId) currentIdArea.take(LinTraParameters.CURRENT_AREA_ID);
		}
		double id0 = cid.getCurrentId();
		cid.increase(numberOfIds);
		Range r = new Range(id0, id0+numberOfIds-1);
		
		currentIdArea.write(cid);
		return r;
	}
}
