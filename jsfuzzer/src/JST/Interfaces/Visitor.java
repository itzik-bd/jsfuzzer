package JST.Interfaces;

import JST.*;
import JST.Switch.CaseBlock;
import JST.VarDecleration.VarDeclerator;

public interface Visitor
{
	public Object visit(Program program);
	
	/* Statement Nodes */
	
	public Object visit(If ifStatement);

	public Object visit(While whileStatement);

	public Object visit(DoWhile doWhile);

	public Object visit(For forStatement);

	public Object visit(ForEach forEach);
	
	public Object visit(Switch switchStatement);
	public Object visit(CaseBlock caseBlock);
	
	public Object visit(FunctionDefinition functionDefinition);
	
	public Object visit(VarDecleration varDecleration);
	public Object visit(VarDeclerator varDeclerator);
	
	public Object visit(Continue continueStatement);

	public Object visit(Break breakStatement);

	public Object visit(Return returnStatement);

	public Object visit(StatementsBlock stmtBlock);
	
	/* Expression Nodes */
	
	public Object visit(Assignment assignment);
	
	public Object visit(CompoundAssignment assignment);
	
	public Object visit(Call call);

	public Object visit(FunctionExpression functionExpression);

	public Object visit(MemberExpression memberExpr);

	public Object visit(ObjectExpression objExpr);
	
	public Object visit(ArrayExpression arrayExpr);
	
	public Object visit(Identifier id);
	
	public Object visit(This thisExpr);

	public Object visit(UnaryOp unaryOp);

	public Object visit(BinaryOp binaryOp);

	public Object visit(TrinaryOp trinaryOp);

	public Object visit(Literal literal);
	public Object visit(LiteralString literal);
	public Object visit(LiteralNumber literal);
}