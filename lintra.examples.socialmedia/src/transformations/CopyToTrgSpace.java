package transformations;

import java.util.Collection;

import blackboard.BlackboardException;
import blackboard.IArea;
import blackboard.IdentifiableElement;
import transfo.IMaster;
import transfo.ITransformation;

public class CopyToTrgSpace implements ITransformation {

	IArea trgArea;
	
	public CopyToTrgSpace(IArea trgArea) {
		this.trgArea = trgArea;
	}
	
	@Override
	public void transform(Collection<IdentifiableElement> objs,
			IMaster masterNextTransfo) throws BlackboardException,
			InterruptedException {
		
		trgArea.writeAll(objs);

	}

}
