package transformations;

import java.util.LinkedList;
import java.util.List;

import blackboard.BlackboardException;
import blackboard.IdentifiableElement;
import runners.MTLauncherInplace;
import socialmedia.Follow;
import socialmedia.Item;
import socialmedia.Like;
import socialmedia.Photo;
import socialmedia.Product;
import socialmedia.Tweet;
import socialmedia.User;
import transfo.ITransformation;
import transfo.LinTraParameters;
import uncertaintypes.UInteger;
import uncertaintypes.UReal;

public class SocialMediaRunner {
	
	public static void main(String[] args) throws Exception {
		
		MTLauncherInplace mtli = new MTLauncherInplace();
		mtli.createBlackboard();
		List<IdentifiableElement> model = createModel();
		mtli.loadModel(model);
//		mtli.getSrcArea().print();
	
		System.out.println("Num elems in initial model: "+mtli.getSrcArea().size());
		ITransformation copy = new CopyToTrgSpace(mtli.getTrgArea());
		double time1 = mtli.launch(copy, null, LinTraParameters.NUMBER_OF_THREADS_T1);

		
		ITransformation t = new SocialMedia(mtli.getSrcArea(), mtli.getTrgArea(), mtli.getCurrentIdArea(), mtli.getIdCorrespondencesArea(), mtli.getDeletesArea());
		double time2 = mtli.launch(t, null, LinTraParameters.NUMBER_OF_THREADS_T1);
		System.out.println("Time: "+(time1+time2));
		System.out.println("Num elems in model after transformation: "+mtli.getTrgArea().size());
//		mtli.getTrgArea().print();
//		System.out.println(mtli.getTrgArea().size());
		
		mtli.destroy();
	}

	private static List<IdentifiableElement> createModel() throws BlackboardException {
		List<IdentifiableElement> model = new LinkedList<IdentifiableElement>();
		
		double idCounter = 1;
		
		double numUsers = 300; //200, 300, 400, 500 -- this is the parameter I've changed to create the input models
		double numFollowersFirstHalf = 10;
		double numFollowersSecondHalf = 200;
		double numLikesPerUser = 50;
		double numItems = 100;

		List<User> users = new LinkedList<User>();
		
			
			for (int userNum = 0; userNum < numUsers; userNum++) {
				User u1 = new User(); u1.setId(Double.toString(idCounter)); idCounter++; model.add(u1); 
				users.add(u1);
			}
			
			for (int userNum = 0; userNum < numUsers; userNum++) {
				User u1 = users.get(userNum);
				/** Followers */
				if (userNum<numUsers/2){
					for (int followNum = 0; followNum < numFollowersFirstHalf; followNum++) {
						Follow f1 = new Follow(0.95); f1.setId(Double.toString(idCounter)); idCounter++;
	//					f1.setFollowedID(Double.toString(followNum-1000));
						double u2 = numUsers%followNum;
						if (u2>0){
							f1.setFollowerID(Double.toString(u2));
							List<String> l = u1.getFollowedByIDs(); l.add(f1.getId());
							u1.setFollowedByIDs(l);
							
							model.add(f1);
						}
//						u1.setFollowsIDs();
					}
				} else {
					for (int followNum = 0; followNum < numFollowersSecondHalf; followNum++) {
						Follow f1 = new Follow(0.99); f1.setId(Double.toString(idCounter)); idCounter++; 
	//					f1.setFollowedID(Double.toString(followNum-1000));
						double u2 = numUsers%followNum;
						if (u2>0){
							f1.setFollowerID(Double.toString(u2));
							List<String> l = u1.getFollowedByIDs(); l.add(f1.getId());
							u1.setFollowedByIDs(l);
							
							model.add(f1);
						}
	//					u1.setFollowsIDs();
					}
				}
			}

			double firstItem = idCounter;
			for (int itemNum = 0; itemNum < numItems; itemNum++) {
				double mod = idCounter % 3;
				Item item;
				if (mod==0){
					item = new Photo(); 
				} else if (mod==1){
					item = new Product();
				} else {
					item = new Tweet(); 
				}
				item.setId(Double.toString(idCounter));
				idCounter++;
				model.add(item);
//				item.setLikesIDs(null);
			}
			
			for (int userNum = 0; userNum < numUsers; userNum++) {
				User u1 = users.get(userNum);
				/** Likes */
				for (int likeNum = 0; likeNum < numLikesPerUser; likeNum++) {
					Like l = new Like(); l.setId(Double.toString(idCounter)); idCounter++; model.add(l);
					l.setConfidence(0.99);
					l.setUserID(u1.getId());
					double itemID = firstItem+(likeNum%numItems);
					l.setItemID(Double.toString(itemID));
					
					List<String> list = u1.getLikesIDs(); list.add(l.getId());
					u1.setLikesIDs(list);
				}
			}
		
		return model;
		
	}
}
