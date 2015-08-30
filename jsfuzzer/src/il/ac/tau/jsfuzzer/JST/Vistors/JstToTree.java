package il.ac.tau.jsfuzzer.JST.Vistors;

import il.ac.tau.jsfuzzer.JST.AbsExpression;
import il.ac.tau.jsfuzzer.JST.ArrayExp;
import il.ac.tau.jsfuzzer.JST.Assignment;
import il.ac.tau.jsfuzzer.JST.Break;
import il.ac.tau.jsfuzzer.JST.Call;
import il.ac.tau.jsfuzzer.JST.Case;
import il.ac.tau.jsfuzzer.JST.CaseBlock;
import il.ac.tau.jsfuzzer.JST.Comment;
import il.ac.tau.jsfuzzer.JST.CompoundAssignment;
import il.ac.tau.jsfuzzer.JST.Continue;
import il.ac.tau.jsfuzzer.JST.Default;
import il.ac.tau.jsfuzzer.JST.DoWhile;
import il.ac.tau.jsfuzzer.JST.For;
import il.ac.tau.jsfuzzer.JST.ForEach;
import il.ac.tau.jsfuzzer.JST.FunctionDef;
import il.ac.tau.jsfuzzer.JST.FunctionExp;
import il.ac.tau.jsfuzzer.JST.Identifier;
import il.ac.tau.jsfuzzer.JST.If;
import il.ac.tau.jsfuzzer.JST.JSTNode;
import il.ac.tau.jsfuzzer.JST.Literal;
import il.ac.tau.jsfuzzer.JST.LiteralNumber;
import il.ac.tau.jsfuzzer.JST.LiteralString;
import il.ac.tau.jsfuzzer.JST.MemberExp;
import il.ac.tau.jsfuzzer.JST.ObjectExp;
import il.ac.tau.jsfuzzer.JST.OperationExp;
import il.ac.tau.jsfuzzer.JST.OutputStatement;
import il.ac.tau.jsfuzzer.JST.Program;
import il.ac.tau.jsfuzzer.JST.RawCode;
import il.ac.tau.jsfuzzer.JST.Return;
import il.ac.tau.jsfuzzer.JST.StatementsBlock;
import il.ac.tau.jsfuzzer.JST.Switch;
import il.ac.tau.jsfuzzer.JST.This;
import il.ac.tau.jsfuzzer.JST.VarDecleration;
import il.ac.tau.jsfuzzer.JST.VarDeclerator;
import il.ac.tau.jsfuzzer.JST.While;
import il.ac.tau.jsfuzzer.JST.Interfaces.JSTObject;
import il.ac.tau.jsfuzzer.JST.Interfaces.ObjectKeys;
import il.ac.tau.jsfuzzer.JST.Interfaces.Visitor;

import java.util.List;
import java.util.Map.Entry;

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
	
	/**
	 * 
	 * @param str
	 * @param node
	 * @return
	 */
	private Object trace(String str, JSTNode node) 
	{
		if(node.isRandomNode() && node.isRandomBranch())
		{
			_s.append(String.format("%3d", _depth));
			for (int i = 0; i < _depth; i++)
				_s.append(" ");
			_s.append(str+"\n");
		}
		
		return null;
	}
	
	private boolean traceIn(String str, JSTNode node)
	{
		trace(str, node);

		if(node.isRandomBranch() && node.isRandomNode()) 
			_depth++;
		
		return node.isRandomBranch();
	}
	
	private void traceOut(JSTNode node) {
		if (node.isRandomNode() && node.isRandomBranch()) {
			_depth--;
		}
	}
	
	private void visitChildrenList(List<? extends JSTObject> list) {
		for (JSTObject node : list)
			node.accept(this, null);		
	}
	
	// ----------------------------------------------------
	
	@Override
	public Object visit(Program program, Object context) 
	{
		if (traceIn("Program", program))
		{
			visitChildrenList(program.getStatements());
			traceOut(program);
		}
		
		return null;
	}

	@Override
	public Object visit(Comment comment, Object context) 
	{
		return trace("Comment", comment);
	}

	@Override
	public Object visit(If ifStatement, Object context)
	{
		if (traceIn("If", ifStatement))
		{
			ifStatement.getCondition().accept(this, null);
			ifStatement.getStatementsBlock().accept(this, null);
			if (ifStatement.hasElse())
				ifStatement.getElseStatementsBlock().accept(this, null);
			traceOut(ifStatement);
		}
		
		return null;
	}

	@Override
	public Object visit(While whileStatement, Object context)
	{
		if (traceIn("While", whileStatement))
		{
			whileStatement.getCondition().accept(this, null);
			whileStatement.getStatementsBlock().accept(this, null);
			traceOut(whileStatement);			
		}
		
		return null;
	}

	@Override
	public Object visit(DoWhile doWhile, Object context)
	{
		if (traceIn("DoWhile", doWhile))
		{
			doWhile.getCondition().accept(this, null);
			doWhile.getStatementsBlock().accept(this, null);
			traceOut(doWhile);			
		}
		
		return null;
	}

	@Override
	public Object visit(For forStatement, Object context)
	{
		if (traceIn("For", forStatement))
		{
			forStatement.getInitStatement().accept(this, null);
			forStatement.getConditionExpression().accept(this, null);
			forStatement.getStepExpression().accept(this, null);
			forStatement.getStatementsBlock().accept(this, null);
			traceOut(forStatement);			
		}
		
		return null;
	}

	@Override
	public Object visit(ForEach forEach, Object context)
	{
		if (traceIn("ForEach", forEach))
		{
			forEach.getCollection().accept(this, null);
			forEach.getItem().accept(this, null);
			forEach.getStatementsBlock().accept(this, null);
			traceOut(forEach);
		}
		
		return null;
	}

	@Override
	public Object visit(Switch switchStatement, Object context) 
	{
		if (traceIn("Switch", switchStatement))
		{
			switchStatement.getExpression().accept(this, null);
			visitChildrenList(switchStatement.getCasesOps());
			traceOut(switchStatement);
		}
		
		return null;
	}

	@Override
	public Object visit(CaseBlock caseBlock, Object context)
	{
		if (traceIn("CaseBlock", caseBlock))
		{
			visitChildrenList(caseBlock.getCases());
			caseBlock.getStatementBlock().accept(this, null);
			traceOut(caseBlock);
		}
		
		return null;
	}

	@Override
	public Object visit(Case myCase, Object context)
	{
		if (traceIn("Case", myCase))
		{
			myCase.getCaseExpr().accept(this, null);
			traceOut(myCase);
		}
		
		return null;
	}

	@Override
	public Object visit(Default myDefault, Object context)
	{
		return trace("Default", myDefault);
	}

	@Override
	public Object visit(FunctionDef functionDefinition, Object context) 
	{
		if (traceIn("FunctionDef", functionDefinition))
		{
			functionDefinition.getId().accept(this, null);
			visitChildrenList(functionDefinition.getFormals());
			functionDefinition.getStatementsBlock().accept(this, null);
			traceOut(functionDefinition);
		}

		return null;
	}

	@Override
	public Object visit(VarDecleration varDecleration, Object context) 
	{
		if(traceIn("VarDecleration", varDecleration))
		{
			visitChildrenList(varDecleration.getDecleratorList());
			traceOut(varDecleration);
		}
		
		return null;
	}

	@Override
	public Object visit(VarDeclerator varDeclerator, Object context)
	{
		if(traceIn("VarDeclerator", varDeclerator))
		{
			varDeclerator.getId().accept(this, null);
			if (varDeclerator.hasInit())
				varDeclerator.getInit().accept(this, null);
			traceOut(varDeclerator);			
		}
		
		return null;
	}

	@Override
	public Object visit(Continue continueStatement, Object context)
	{
		return trace("Continue", continueStatement);
	}

	@Override
	public Object visit(Break breakStatement, Object context) 
	{
		return trace("Break", breakStatement);
	}

	@Override
	public Object visit(Return returnStatement, Object context)
	{
		if (traceIn("Return", returnStatement))
		{
			if (returnStatement.hasValue())
				returnStatement.getValue().accept(this, null);
			traceOut(returnStatement);			
		}
		
		return null;
	}

	@Override
	public Object visit(StatementsBlock stmtBlock, Object context)
	{
		if (traceIn("StatementsBlock", stmtBlock))
		{
			visitChildrenList(stmtBlock.getStatements());
			traceOut(stmtBlock);
		}
		
		return null;
	}

	@Override
	public Object visit(Assignment assignment, Object context)
	{
		if (traceIn("Assignment", assignment))
		{
			assignment.getLeftHandSide().accept(this, null);
			assignment.getExpr().accept(this, null);
			traceOut(assignment);
		}
		
		return null;
	}

	@Override
	public Object visit(CompoundAssignment assignment, Object context) 
	{
		if (traceIn(String.format("CompoundAssignment (%s)", assignment.getCompoundOp()), assignment))
		{
			assignment.getLeftHandSide().accept(this, null);
			assignment.getExpr().accept(this, null);
			traceOut(assignment);
		}
		
		return null;
	}

	@Override
	public Object visit(Call call, Object context)
	{
		if (traceIn("Call", call))
		{
			call.getBase().accept(this, null);
			visitChildrenList(call.getParams());
			traceOut(call);
		}
		
		return null;
	}

	@Override
	public Object visit(FunctionExp functionExpression, Object context)
	{
		if (traceIn("FunctionExp", functionExpression))
		{
			visitChildrenList(functionExpression.getFormals());
			functionExpression.getStatementsBlock().accept(this, null);
			traceOut(functionExpression);
		}
		
		return null;
	}

	@Override
	public Object visit(MemberExp memberExpr, Object context) 
	{
		if (traceIn("MemberExp", memberExpr))
		{
			memberExpr.getBase().accept(this, null);
			memberExpr.getLocation().accept(this, null);
			traceOut(memberExpr);
		}
		
		return null;
	}

	@Override
	public Object visit(ObjectExp objExpr, Object context)
	{
		if (traceIn("ObjectExp", objExpr))
		{
			for (Entry<ObjectKeys, AbsExpression> item : objExpr.getMap().entrySet())
			{
				item.getKey().accept(this, null);
				item.getValue().accept(this, null);
			}
			traceOut(objExpr);
		}
		
		return null;
	}

	@Override
	public Object visit(ArrayExp arrayExpr, Object context)
	{
		if (traceIn("ArrayExp", arrayExpr))
		{
			visitChildrenList(arrayExpr.getItemsList());
			traceOut(arrayExpr);
		}
		
		return null;
	}

	@Override
	public Object visit(Identifier id, Object context) 
	{
		return trace(String.format("Identifier (%s)", id), id);
	}

	@Override
	public Object visit(This thisExpr, Object context)
	{
		return trace("This", thisExpr);
	}

	@Override
	public Object visit(OperationExp opExp, Object context) 
	{
		if (traceIn(String.format("OperationExp (%s)", opExp.getOperator()), opExp))
		{
			visitChildrenList(opExp.getOperandList());
			traceOut(opExp);
		}
		
		return null;
	}

	@Override
	public Object visit(Literal literal, Object context)
	{
		return trace(String.format("Literal (%s)", literal.getType()), literal);
	}

	@Override
	public Object visit(LiteralString literal, Object context)
	{
		return trace("LiteralString", literal);
	}

	@Override
	public Object visit(LiteralNumber literal, Object context)
	{
		return trace(String.format("LiteralNumber (%s)", literal), literal);
	}

	@Override
	public Object visit(OutputStatement outputStmt, Object context)
	{
		return trace("OutputStatement", outputStmt);
	}

	@Override
	public Object visit(RawCode rawcode, Object context)
	{
		/* do nothing - should not be called */
		return null;
	}
}