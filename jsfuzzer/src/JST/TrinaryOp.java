package JST;

import JST.Enums.TrinaryOps;
import JST.Interfaces.Assignable;
import JST.Interfaces.Visitor;

public class TrinaryOp extends AbsExpression implements Assignable
{
	private AbsExpression operand1;

	private AbsExpression operand2;

	private AbsExpression operand3;

	private TrinaryOps operator;


	/**
	 * Constructs a new binary operation node. Used by subclasses.
	 * 
	 * @param operand1
	 *            The first operand.
	 * @param operand2
	 *            The second operand.
	 * @param operand3
	 *            The third operand.
	 * @param operator
	 *            The operator.
	 */
	public TrinaryOp(TrinaryOps operator, AbsExpression operand1, AbsExpression operand2, AbsExpression operand3)
	{
		this.operand1 = operand1;
		this.operand2 = operand2;
		this.operand3 = operand3;
		this.operator = operator;
	}

	public TrinaryOps getOperator() 
	{
		return operator;
	}

	public AbsExpression getFirstOperand() 
	{
		return operand1;
	}

	public AbsExpression getSecondOperand() 
	{
		return operand2;
	}
	
	public AbsExpression getThirdOperand()
	{
		return operand3;
	}
	
	@Override
	public Object accept(Visitor visitor, Object context) {
		return visitor.visit(this, context);
	}
}
