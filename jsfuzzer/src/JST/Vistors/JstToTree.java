package JST.Vistors;

import java.util.List;
import java.util.Map.Entry;

import JST.AbsExpression;
import JST.ArrayExp;
import JST.Assignment;
import JST.Break;
import JST.Call;
import JST.Case;
import JST.CaseBlock;
import JST.Comment;
import JST.CompoundAssignment;
import JST.Continue;
import JST.Default;
import JST.DoWhile;
import JST.For;
import JST.ForEach;
import JST.FunctionDef;
import JST.FunctionExp;
import JST.Identifier;
import JST.If;
import JST.Literal;
import JST.LiteralNumber;
import JST.LiteralString;
import JST.MemberExp;
import JST.ObjectExp;
import JST.OperationExp;
import JST.OutputStatement;
import JST.Program;
import JST.Return;
import JST.StatementsBlock;
import JST.Switch;
import JST.This;
import JST.VarDecleration;
import JST.VarDeclerator;
import JST.While;
import JST.Interfaces.JSTObject;
import JST.Interfaces.ObjectKeys;
import JST.Interfaces.Visitor;

public class JstToTree implements Visitor
{
	private int _depth = 0;
	private StringBuffer _s = new StringBuffer();
	
	public static String execute(Program program)
	{
		JstToTree convertor = new JstToTree();
		program.accept(convertor, null);
		return convertor._s.toString(); 
	}
	
	private Object trace(String str) {
		_s.append(String.format("%3d", _depth));
		for (int i = 0; i < _depth; i++)
			_s.append(" ");
		_s.append(str+"\n");
		return null;
	}
	
	private void traceIn(String str) {
		trace(str);
		_depth++;
	}
	
	private Object traceOut() {
		_depth--;
		return null;
	}
	
	private void visitChildrenList(List<? extends JSTObject> list) {
		for (JSTObject node : list)
			node.accept(this, null);		
	}
	
	// ----------------------------------------------------
	
	@Override
	public Object visit(Program program, Object context) {
		traceIn("Program");
		visitChildrenList(program.getStatements());
		return traceOut();
	}

	@Override
	public Object visit(Comment comment, Object context) {
		traceIn("Comment");
		return traceOut();
	}

	@Override
	public Object visit(If ifStatement, Object context) {
		if (ifStatement.isNonRandomBranch())
			return null;
		
		traceIn("If");
		ifStatement.getCondition().accept(this, null);
		ifStatement.getStatementsBlock().accept(this, null);
		if (ifStatement.hasElse())
			ifStatement.getElseStatementsBlock().accept(this, null);
		return traceOut();
	}

	@Override
	public Object visit(While whileStatement, Object context) {
		traceIn("While");
		whileStatement.getCondition().accept(this, null);
		whileStatement.getStatementsBlock().accept(this, null);
		return traceOut();
	}

	@Override
	public Object visit(DoWhile doWhile, Object context) {
		traceIn("DoWhile");
		doWhile.getCondition().accept(this, null);
		doWhile.getStatementsBlock().accept(this, null);
		return traceOut();
	}

	@Override
	public Object visit(For forStatement, Object context) {
		traceIn("For");
		forStatement.getInitStatement().accept(this, null);
		forStatement.getConditionExpression().accept(this, null);
		forStatement.getStepExpression().accept(this, null);
		forStatement.getStatementsBlock().accept(this, null);
		return traceOut();
	}

	@Override
	public Object visit(ForEach forEach, Object context) {
		traceIn("ForEach");
		forEach.getCollection().accept(this, null);
		forEach.getItem().accept(this, null);
		forEach.getStatementsBlock().accept(this, null);
		return traceOut();
	}

	@Override
	public Object visit(Switch switchStatement, Object context) {
		traceIn("Switch");
		switchStatement.getExpression().accept(this, null);
		visitChildrenList(switchStatement.getCasesOps());
		return traceOut();
	}

	@Override
	public Object visit(CaseBlock caseBlock, Object context) {
		traceIn("CaseBlock");
		visitChildrenList(caseBlock.getCases());
		caseBlock.getStatementBlock().accept(this, null);
		return traceOut();
	}

	@Override
	public Object visit(Case myCase, Object context) {
		traceIn("Case");
		myCase.getCaseExpr().accept(this, null);
		return traceOut();
	}

	@Override
	public Object visit(Default myDefault, Object context) {
		return trace("Default");
	}

	@Override
	public Object visit(FunctionDef functionDefinition, Object context) {
		traceIn("FunctionDef");
		functionDefinition.getId().accept(this, null);
		visitChildrenList(functionDefinition.getFormals());
		functionDefinition.getStatementsBlock().accept(this, null);
		return traceOut();
	}

	@Override
	public Object visit(VarDecleration varDecleration, Object context) {
		traceIn("VarDecleration");
		visitChildrenList(varDecleration.getDecleratorList());
		return traceOut();
	}

	@Override
	public Object visit(VarDeclerator varDeclerator, Object context) {
		traceIn("VarDeclerator");
		varDeclerator.getId().accept(this, null);
		if (varDeclerator.hasInit())
			varDeclerator.getInit().accept(this, null);
		return traceOut();
	}

	@Override
	public Object visit(Continue continueStatement, Object context) {
		return trace("Continue");
	}

	@Override
	public Object visit(Break breakStatement, Object context) {
		return trace("Break");
	}

	@Override
	public Object visit(Return returnStatement, Object context) {
		traceIn("Return");
		if (returnStatement.hasValue())
			returnStatement.getValue().accept(this, null);
		return traceOut();
	}

	@Override
	public Object visit(StatementsBlock stmtBlock, Object context) {
		traceIn("StatementsBlock");
		visitChildrenList(stmtBlock.getStatements());
		return traceOut();
	}

	@Override
	public Object visit(Assignment assignment, Object context) {
		traceIn("Assignment");
		assignment.getLeftHandSide().accept(this, null);
		assignment.getExpr().accept(this, null);
		return traceOut();
	}

	@Override
	public Object visit(CompoundAssignment assignment, Object context) {
		traceIn(String.format("CompoundAssignment (%s)", assignment.getCompoundOp()));
		assignment.getLeftHandSide().accept(this, null);
		assignment.getExpr().accept(this, null);
		return traceOut();
	}

	@Override
	public Object visit(Call call, Object context) {
		traceIn("Call");
		call.getBase().accept(this, null);
		visitChildrenList(call.getParams());
		return traceOut();
	}

	@Override
	public Object visit(FunctionExp functionExpression, Object context) {
		traceIn("FunctionExp");
		visitChildrenList(functionExpression.getFormals());
		functionExpression.getStatementsBlock().accept(this, null);
		return traceOut();
	}

	@Override
	public Object visit(MemberExp memberExpr, Object context) {
		traceIn("MemberExp");
		memberExpr.getBase().accept(this, null);
		memberExpr.getLocation().accept(this, null);
		return traceOut();
	}

	@Override
	public Object visit(ObjectExp objExpr, Object context) {
		traceIn("ObjectExp");
		for (Entry<ObjectKeys, AbsExpression> item : objExpr.getMap().entrySet()) {
			item.getKey().accept(this, null);
			item.getValue().accept(this, null);
		}
		return traceOut();
	}

	@Override
	public Object visit(ArrayExp arrayExpr, Object context) {
		traceIn("ArrayExp");
		visitChildrenList(arrayExpr.getItemsList());
		return traceOut();
	}

	@Override
	public Object visit(Identifier id, Object context) {
		return trace(String.format("Identifier (%s)", id));
	}

	@Override
	public Object visit(This thisExpr, Object context) {
		return trace("This");
	}

	@Override
	public Object visit(OperationExp opExp, Object context) {
		traceIn(String.format("OperationExp (%s)", opExp.getOperator()));
		visitChildrenList(opExp.getOperandList());
		return traceOut();
	}

	@Override
	public Object visit(Literal literal, Object context) {
		return trace(String.format("Literal (%s)", literal.getType()));
	}

	@Override
	public Object visit(LiteralString literal, Object context) {
		return trace("LiteralString");
	}

	@Override
	public Object visit(LiteralNumber literal, Object context) {
		return trace(String.format("LiteralNumber (%s)", literal));
	}

	@Override
	public Object visit(OutputStatement outputStmt, Object context) {
		return trace("OutputStatement");
	}

}
