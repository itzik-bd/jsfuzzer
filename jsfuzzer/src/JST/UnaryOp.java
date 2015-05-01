package JST;

import JST.Enums.UnaryOps;

public class UnaryOp extends AbsExpression
{
	private AbsExpression operand;

	private UnaryOps operator;

	/**
	 * Constructs a new binary operation node. Used by subclasses.
	 * 
	 * @param operand
	 *            The operand.
	 * @param operator
	 *            The operator.
	 */
	
	public UnaryOp(AbsExpression operand, UnaryOps operator)
	{
		this.operand = operand;
		this.operator = operator;
	}

	public UnaryOps getOperator() {
		return operator;
	}

	public AbsExpression getOperand() {
		return operand;
	}
}