package transformations.confidence.smarthome;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import blackboard.*;
import mm.confidence.smarthome.COHigh;
import mm.confidence.smarthome.Home;
import mm.confidence.smarthome.TempIncr;
import mm.confidence.smarthome.TempWarning;
import transfo.*;
import uncertaintypes.UBoolean;
import uncertaintypes.UInteger;
import uncertaintypes.UReal;

public class SmartHome implements ITransformation {

	private static final UReal SECS = new UReal(1,0);
	
	private IArea srcArea, trgArea, currentIdArea, idCorrespondencesArea, deletesArea;
	
	public SmartHome(IArea srcArea, IArea trgArea, IArea currentIdArea, IArea correspondencesArea, IArea deletesArea) {
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
			if (ie instanceof Home){
				Home h1 = (Home) ie;
				List<Home> homes = readHomes();
				for(Home h2 : homes){
					UBoolean matching = h2.getTemp().minus(h1.getTemp()).ge(new UReal(2, 0)).and(
							h2.getTs().gt(h1.getTs())).and(
							h2.getTs().minus(h1.getTs()).lt(new UReal(60,0)));
					if (h1.gethIdentifier().equals(h2.gethIdentifier())){
						if (matching.getC()>0.5){
							/** Mathing TempIncr*/
//							System.out.println("matching temperatureIncrease");
							TempIncr ti = new TempIncr();
							ti.setConfidence(h1.getConfidence()*h2.getConfidence()*matching.getC()*0.925);
							ti.setTs(h2.getTs());
							ti.setTemp(h2.getTemp());
							ti.setIncr(h2.getTemp().minus(h1.getTemp()));
							createdElems.add(ti);
						}
					}
				}
			}
			if (ie instanceof TempIncr) {
				TempIncr t1 = (TempIncr) ie;
				List<TempIncr> tis = readTempIncr();
				for (TempIncr t2 : tis){
					if (t1!=t2){
					UBoolean matching = t2.getTemp().gt(t1.getTemp()).and(t2.getTs().gt(t1.getTs()));
					if (t1.getHomeID().equals(t2.getHomeID()) && matching.toBoolean()){
						for (TempIncr t3 : tis){
							if (t3!=t1 && t3!=t2){
							matching = matching.and(t3.getTemp().gt(t2.getTemp())).and(t3.getTs().gt(t2.getTs()));
							if (t2.getHomeID().equals(t3.getHomeID()) && matching.toBoolean()){
								for (TempIncr t4 : tis){
									if (t4!=t1 && t4!=t2 && t4!=t3){
									matching = matching.and(t4.getTemp().gt(t3.getTemp()).and(t4.getTs().gt(t3.getTs())));
									if (t3.getHomeID().equals(t4.getHomeID()) && matching.toBoolean()){
										/** Mathing TempWarning */
										TempWarning tw = new TempWarning();
										tw.setHomeID(t1.getHomeID());
										tw.setTemp(t4.getTemp());
										tw.setTs(t4.getTs());
										tw.setConfidence(t1.getConfidence()*t2.getConfidence()*t3.getConfidence()*t4.getConfidence()*matching.getC()*0.925);
										createdElems.add(tw);
										
									}
								}
							}
						}
					}}}}
				}
			}
			if (ie instanceof Home) {
				Home h1 = (Home) ie;
				UBoolean matching = h1.getCo().ge(new UReal(5000, 0)); 
				if (matching.toBoolean()){
					/** Mathing COHigh */
					COHigh coh = new COHigh();
					coh.setHomeID(h1.gethIdentifier());
					coh.setTs(h1.getTs());
					coh.setConfidence(matching.getC());
					createdElems.add(coh);
				}
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
	
	
	private List<Home> readHomes() {
		try {
			List<Home> homes = new LinkedList<Home>();
			List<IdentifiableElement> elems = (List<IdentifiableElement>) srcArea.read(srcArea.size());
			for (IdentifiableElement e : elems){
				if (e instanceof Home){
					homes.add((Home) e);
				}
			}
			return homes;
		} catch (BlackboardException e) {
			e.printStackTrace();
		}
		return new LinkedList<Home>();
	}
	
	private List<TempIncr> readTempIncr() {
		try {
			List<TempIncr> tempIncr = new LinkedList<TempIncr>();
			List<IdentifiableElement> elems = (List<IdentifiableElement>) srcArea.read(srcArea.size());
			for (IdentifiableElement e : elems){
				if (e instanceof TempIncr){
					tempIncr.add((TempIncr) e);
				}
			}
			return tempIncr;
		} catch (BlackboardException e) {
			e.printStackTrace();
		}
		return new LinkedList<TempIncr>();
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
