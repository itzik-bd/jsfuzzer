package JST.Interfaces;

import JST.*;

public interface Visitor
{
	public Object visit(Program program, Object context);
	
	public Object visit(Comment program, Object context);
	
	/* Statement Nodes */
	
	public Object visit(If ifStatement, Object context);

	public Object visit(While whileStatement, Object context);

	public Object visit(DoWhile doWhile, Object context);

	public Object visit(For forStatement, Object context);

	public Object visit(ForEach forEach, Object context);
	
	public Object visit(Switch switchStatement, Object context);
	public Object visit(CaseBlock caseBlock, Object context);
	public Object visit(Case myCase, Object context);
	public Object visit(Default myDefault, Object context);
	
	public Object visit(FunctionDef functionDefinition, Object context);
	
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

	public Object visit(FunctionExp functionExpression, Object context);

	public Object visit(MemberExp memberExpr, Object context);

	public Object visit(ObjectExp objExpr, Object context);
	
	public Object visit(ArrayExp arrayExpr, Object context);
	
	public Object visit(Identifier id, Object context);
	
	public Object visit(This thisExpr, Object context);

	public Object visit(OperationExp binaryOp, Object context);

	public Object visit(Literal literal, Object context);
	public Object visit(LiteralString literal, Object context);
	public Object visit(LiteralNumber literal, Object context);

	public Object visit(OutputStatement outputStmt, Object context);
}