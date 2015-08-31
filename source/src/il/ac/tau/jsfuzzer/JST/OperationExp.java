package il.ac.tau.jsfuzzer.JST;

import il.ac.tau.jsfuzzer.JST.Enums.Operator;
import il.ac.tau.jsfuzzer.JST.Interfaces.Visitor;

import java.util.Arrays;
import java.util.List;

public class OperationExp extends AbsExpression
{	
	private Operator _operator;
	private List<AbsExpression> _operandList;
	
	public OperationExp(Operator operator, AbsExpression... operandsList) {
		this(operator, Arrays.asList(operandsList));
	}
	
	public OperationExp(Operator operator, List<AbsExpression> operandsList) {
		if (operator.getNumOperands() != operandsList.size())
			throw new IllegalArgumentException("operands list size is not compatible with the operator " + operator);
		
		_operator = operator;
		_operandList = operandsList;
	}

	public Operator getOperator() {
		return _operator;
	}

	public List<AbsExpression> getOperandList() {
		return _operandList;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}