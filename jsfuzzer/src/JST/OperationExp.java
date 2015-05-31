package JST;

import java.util.Arrays;
import java.util.List;

import JST.Enums.Operator;
import JST.Interfaces.Visitor;

public class OperationExp extends AbsExpression
{	
	private Operator _operator;
	private List<AbsExpression> _operandList;
	
	public OperationExp(Operator operator, AbsExpression... operandsList) {
		if (operator.getNumOperands() != operandsList.length)
			throw new IllegalArgumentException("operands list size is not compatible with the operator " + operator);
		
		_operator = operator;
		_operandList = Arrays.asList(operandsList);
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