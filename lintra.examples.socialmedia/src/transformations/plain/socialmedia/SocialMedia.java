package transformations.plain.socialmedia;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import blackboard.*;
import mm.plain.socialmedia.Follow;
import mm.plain.socialmedia.Item;
import mm.plain.socialmedia.Like;
import mm.plain.socialmedia.User;
import transfo.*;

public class SocialMedia implements ITransformation {

	private static final double NUMFOLLOWERS = 100;
	
	private IArea srcArea, trgArea, currentIdArea, idCorrespondencesArea, deletesArea;
	
	public SocialMedia(IArea srcArea, IArea trgArea, IArea currentIdArea, IArea correspondencesArea, IArea deletesArea) {
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
			if (ie instanceof User){
				List<Item> items = readItems();
				for (Item i : items){
					
					double sum=0;
					User u = (User) ie;
					List<String> l = u.getFollowedByIDs();
					for (String s : l) {
						Follow f = (Follow) srcArea.read(s);
						if (f.getConfidence()>0.95){
							User u2 = (User) srcArea.read(f.getFollowerID());
							for (String likeID : u2.getLikesIDs()){
								Like lk = (Like) srcArea.read(likeID);
								if (lk.getConfidence()>0.85 && lk.getItemID().equals(i.getId())){
									sum++;
								}
							}
						}
					}
					if (sum>NUMFOLLOWERS){
						List<String> likes = u.getLikesIDs();
						for (String lkID : likes){
							Like lk = (Like) srcArea.read(lkID);
							if (!lk.getItemID().equals(i.getId())){
								/** /// Matching fulfilled -- RHS /// */
								Like newLike = new Like();
								newLike.setUserID(u.getId());
								newLike.setItemID(i.getId());
								createdElems.add(newLike);
								modifiedElems.add(u);
								modifiedElems.add(i);
//								System.out.println("apply");
								/** ///// */
							}
						}
					}
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
	
	
	private List<Item> readItems() {
		try {
			List<Item> items = new LinkedList<Item>();
			List<IdentifiableElement> elems = (List<IdentifiableElement>) srcArea.read(srcArea.size());
			for (IdentifiableElement e : elems){
				if (e instanceof Item){
					items.add((Item) e);
				}
			}
			return items;
		} catch (BlackboardException e) {
			e.printStackTrace();
		}
		return new LinkedList<Item>();
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
