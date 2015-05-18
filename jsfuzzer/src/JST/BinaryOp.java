package JST;

import JST.Enums.BinaryOps;
import JST.Interfaces.Visitor;

public class BinaryOp extends AbsExpression
{	
	private AbsExpression operand1;

	private BinaryOps operator;

	private AbsExpression operand2;

	/**
	 * Constructs a new binary operation node. Used by subclasses.
	 * 
	 * @param operand1
	 *            The first operand.
	 * @param operator
	 *            The operator.
	 * @param operand2
	 *            The second operand.
	 */
	public BinaryOp(BinaryOps operator, AbsExpression operand1, AbsExpression operand2)
	{
		this.operand1 = operand1;
		this.operator = operator;
		this.operand2 = operand2;
	}

	public BinaryOps getOperator() {
		return operator;
	}

	public AbsExpression getFirstOperand() {
		return operand1;
	}

	public AbsExpression getSecondOperand() {
		return operand2;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}

}
