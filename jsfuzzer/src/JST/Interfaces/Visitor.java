package JST.Interfaces;

import JST.*;

public interface Visitor
{
	
	public Object visit(Program program);
	
	
	/* Statement Nodes */
	
	public Object visit(FunctionDefinition functionDefinition);
	
	public Object visit(VarDefinition varDefinition);

	public Object visit(While whileStatement);

	public Object visit(DoWhile doWhile);

	public Object visit(For forStatement);

	public Object visit(ForEach forEach);
	
	public Object visit(Continue continueStatement);

	public Object visit(Break breakStatement);

	public Object visit(Switch switchStatement);

	public Object visit(Return returnStatement);

	public Object visit(If ifStatement);

	public Object visit(StatementsBlock stmtBlock);
	

	/* Expression Nodes */
	
	public Object visit(Assignment assignment);
	
	public Object visit(CompoundAssignment assignment);
	
	public Object visit(Call call);

	public Object visit(ArrayExp arrayExp);

	public Object visit(FunctionExpression functionExpression);
	
	public Object visit(Identifier id);

	public Object visit(MemberExpression memberExpr);

	public Object visit(ObjectExpression objExpr);
	
	public Object visit(This thisExpr);

	public Object visit(UnaryOp unaryOp);

	public Object visit(BinaryOp binaryOp);

	public Object visit(TrinaryOp trinaryOp);

	//Add Literals
	
	/* Comment Nodes */
	
	public Object visit(SingleLineComment comment);
	
	public Object visit(MultiLineComment comment);


}