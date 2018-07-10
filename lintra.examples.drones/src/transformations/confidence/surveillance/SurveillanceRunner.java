package transformations.confidence.surveillance;

import java.util.LinkedList;
import java.util.List;

import blackboard.BlackboardException;
import blackboard.IdentifiableElement;
import mm.confidence.surveillance.Clock;
import mm.confidence.surveillance.Coordinate;
import mm.confidence.surveillance.Drone;
import mm.confidence.surveillance.UnidentifiedObject;
import runners.MTLauncherInplace;
import transfo.ITransformation;
import transfo.LinTraParameters;
import uncertaintypes.*;

public class SurveillanceRunner {
	
	public static void main(String[] args) throws Exception {
		run(100);
		run(1000);
		run(10000);
		run(100000);
	}

	private static void run(int initialModelSize) throws BlackboardException, Exception {
		MTLauncherInplace mtli = new MTLauncherInplace();
		mtli.createBlackboard();
		List<IdentifiableElement> model = createModel(initialModelSize/10);
		mtli.loadModel(model);
	
		System.out.println("Num elems in initial model: "+mtli.getSrcArea().size());
		ITransformation copy = new CopyToTrgSpace(mtli.getTrgArea());
		double time1 = mtli.launch(copy, null, LinTraParameters.NUMBER_OF_THREADS_T1);

		ITransformation t = new ConfidenceSurveillance(mtli.getSrcArea(), mtli.getTrgArea(), mtli.getCurrentIdArea(), mtli.getIdCorrespondencesArea(), mtli.getDeletesArea());
		double time2 = mtli.launch(t, null, LinTraParameters.NUMBER_OF_THREADS_T1);
		System.out.println("Num elems in model after transformation: "+mtli.getTrgArea().size());
		System.out.println("Transformation Time: "+(time1+time2)+"\n");
//		mtli.getTrgArea().print();
//		System.out.println(mtli.getTrgArea().size());
		
		mtli.destroy();
	}

	private static List<IdentifiableElement> createModel(int n) throws BlackboardException {
		List<IdentifiableElement> model = new LinkedList<IdentifiableElement>();
		
		double idCounter = 1;
		
		Clock clock = new Clock(); clock.setId(idCounter+""); clock.setNow(new UInteger(0,0));
		model.add(clock); idCounter++;
		
		for (int i = 0; i < n; i++) {
			
			Coordinate c1 = new Coordinate(idCounter+"", new UReal(0+1001*i, 0.1), new UReal(0+1001*i, 0.1)); idCounter++;
			Drone d1 = new Drone(idCounter+"", new UReal(5.0, 0.1), new UReal(0.78, 0.02), new UReal(20, 0.1), c1.getId()); idCounter++;
			model.add(c1); model.add(d1);
			
			Coordinate c2 = new Coordinate(idCounter+"", new UReal(500+1001*i, 0.1), new UReal(700+1001*i, 0.1)); idCounter++;
			Drone d2 = new Drone(idCounter+"", new UReal(9.0, 0.1), new UReal(1.5, 0.02), new UReal(20, 0.1), c2.getId()); idCounter++;
			model.add(c2); model.add(d2);
			
			Coordinate c3 = new Coordinate(idCounter+"", new UReal(700+1001*i, 0.1), new UReal(700+1001*i, 0.1)); idCounter++;
			UnidentifiedObject o1 = new UnidentifiedObject(idCounter+"", new UReal(75.0, 0.1), new UReal(3.92, 0.07), new UReal(50, 0.2), c3.getId(), 0.98); idCounter++;
			model.add(c3);
			model.add(o1);
			
			Coordinate c4 = new Coordinate(idCounter+"", new UReal(1000+1001*i, 0.1), new UReal(900+1001*i, 0.1)); idCounter++;
			UnidentifiedObject o2 = new UnidentifiedObject(idCounter+"", new UReal(90, 0.1), new UReal(3.14, 0.07), new UReal(60, 0.2), c4.getId(), 0.98); idCounter++;
			model.add(c4);
			model.add(o2);
			
			Coordinate c5 = new Coordinate(idCounter+"", new UReal(1000+1001*i, 0.1), new UReal(900+1001*i, 0.1)); idCounter++;
			UnidentifiedObject o3 = new UnidentifiedObject(idCounter+"", new UReal(90, 0.1), new UReal(3.14, 0.07), new UReal(60, 0.2), c5.getId(), 0.98); idCounter++;
			model.add(c5);
			model.add(o3);
			
		}
		return model;
		
	}
}
