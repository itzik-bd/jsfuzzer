package JST.Interfaces;

import JST.*;
import JST.Switch.CaseBlock;
import JST.VarDecleration.VarDeclerator;

public interface Visitor
{
	public Object visit(Program program, Object context);
	
	/* Statement Nodes */
	
	public Object visit(If ifStatement, Object context);

	public Object visit(While whileStatement, Object context);

	public Object visit(DoWhile doWhile, Object context);

	public Object visit(For forStatement, Object context);

	public Object visit(ForEach forEach, Object context);
	
	public Object visit(Switch switchStatement, Object context);
	public Object visit(CaseBlock caseBlock, Object context);
	
	public Object visit(FunctionDefinition functionDefinition, Object context);
	
	public Object visit(VarDecleration varDecleration, Object context);
	public Object visit(VarDeclerator varDeclerator, Object context);
	
	public Object visit(Continue continueStatement, Object context);

	public Object visit(Break breakStatement, Object context);

	public Object visit(Return returnStatement, Object context);

	public Object visit(StatementsBlock stmtBlock, Object context);
	
	/* Expression Nodes */
	
	public Object visit(Assignment assignment, Object context);
	
	public Object visit(CompoundAssignment assignment, Object context);
	
	public Object visit(Call call, Object context);

	public Object visit(FunctionExpression functionExpression, Object context);

	public Object visit(MemberExpression memberExpr, Object context);

	public Object visit(ObjectExpression objExpr, Object context);
	
	public Object visit(ArrayExpression arrayExpr, Object context);
	
	public Object visit(Identifier id, Object context);
	
	public Object visit(This thisExpr, Object context);

	public Object visit(UnaryOp unaryOp, Object context);

	public Object visit(BinaryOp binaryOp, Object context);

	public Object visit(TrinaryOp trinaryOp, Object context);

	public Object visit(Literal literal, Object context);
	public Object visit(LiteralString literal, Object context);
	public Object visit(LiteralNumber literal, Object context);
}