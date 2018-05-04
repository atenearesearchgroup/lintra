package transfo;

import java.util.Collection;
import java.util.List;

import blackboard.BlackboardException;
import blackboard.IdentifiableElement;

public interface ITransformation {

	public void transform(
			Collection<IdentifiableElement> objs, IMaster masterNextTransfo) throws BlackboardException, InterruptedException;

}
