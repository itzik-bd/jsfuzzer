package JST.Vistors;

import java.util.List;
import java.util.Map.Entry;

import JST.*;
import JST.Interfaces.JSTObject;
import JST.Interfaces.ObjectKeys;
import JST.Interfaces.Visitor;

import org.apache.commons.lang3.StringEscapeUtils;

public class JstToJs implements Visitor
{
	private final String IDENT;
	private final String EOL;
	
	private int _depth = 0;
	
	// --------------------------------------------
	
	public static String execute(Program program)
	{
		JstToJs convertor = new JstToJs("\t", "\n");
		return convertor.run(program);
	}
	
	public static String executeMinify(Program program)
	{
		JstToJs convertor = new JstToJs("", "");
		return convertor.run(program);
	}
	
	public static String executeCostum(Program program, String IDENT, String EOL)
	{
		JstToJs convertor = new JstToJs(IDENT, EOL);
		return convertor.run(program);
	}
	
	// --------------------------------------------
	
	public JstToJs(String IDENT, String EOL)
	{
		this.IDENT = IDENT;
		this.EOL = EOL;
	}
	
	public String run(Program program)
	{
		return (String) visit(program, false);
	}
	
	// --------------------------------------------
	
	@Override
	public Object visit(Program program, Object isStatement)
	{
		return listJoin(program.getStatements(), true).substring(1);
	}
	
	@Override
	public Object visit(Comment comment, Object isStatement)
	{
		String[] commentLines = comment.getComment().split("\\r?\\n");;
		StringBuffer s = new StringBuffer();
		
		if (commentLines.length == 1)
		{
			s.append("/* " + commentLines[0] + " */");
		}
		else if (commentLines.length > 1)
		{
			ident(s);
			s.append("/*"); // begin comment
			
			for (String line : commentLines)
			{
				ident(s);
				s.append(" * " + line);
			}
			
			ident(s);
			s.append(" */"); // end comment
		}
		
		return s.toString();
	}

	@Override
	public Object visit(FunctionDefinition functionDefinition, Object isStatement)
	{
		StringBuffer s = new StringBuffer();
		
		ident(s);
		s.append(String.format("function %s(%s) ", functionDefinition.getId().accept(this, false), listJoin(functionDefinition.getFormals(), false, ", ")));
		_depth++;
		s.append(functionDefinition.getStatementsBlock().accept(this, true));
		_depth--;

		return s.toString();
	}

	@Override
	public Object visit(VarDecleration varDefinition, Object isStatement)
	{
		String res = String.format("var %s", listJoin(varDefinition.getDecleratorList(), false, ", "));
		
		if (isTrue(isStatement))
			res = identString(res + ";");
		
		return res;
	}
	
	@Override
	public Object visit(VarDeclerator varDeclerator, Object isStatement)
	{
		String res;
		
		if (varDeclerator.hasInit())
			res = String.format("%s = %s", varDeclerator.getId().accept(this, false), varDeclerator.getInit().accept(this, false));
		else
			res = (String) varDeclerator.getId().accept(this, false);
		
		return res;
	}

	@Override
	public Object visit(While whileStatement, Object isStatement)
	{
		StringBuffer s = new StringBuffer();
		
		ident(s);
		s.append(whileStatement.getLoopCounterInit().accept(this, true)); //init the loop counter outside the loop
		ident(s);
		s.append(String.format("while (%s) ", whileStatement.getCondition().accept(this, false)));
		_depth++;
		s.append(whileStatement.getStatementsBlock().accept(this, true));
		_depth--;
		
		return s.toString();
	}

	@Override
	public Object visit(DoWhile doWhile, Object isStatement)
	{
		StringBuffer s = new StringBuffer();
		
		ident(s);
		s.append(doWhile.getLoopCounterInit().accept(this, true)); //init the loop counter outside the loop
		ident(s);
		s.append("do ");
		_depth++;
		s.append(doWhile.getStatementsBlock().accept(this, true));
		_depth--;
		ident(s);
		s.append(String.format("while (%s);",doWhile.getCondition().accept(this, false)));
		
		return s.toString();
	}

	@Override
	public Object visit(For forStatement, Object isStatement)
	{
		StringBuffer s = new StringBuffer();
		
		ident(s);
		s.append(String.format("for (%s ; %s ; %s) ", forStatement.getInitStatement().accept(this, false), forStatement.getConditionExpression().accept(this, false), forStatement.getStepExpression().accept(this, false)));
		_depth++;
		s.append(forStatement.getStatementsBlock().accept(this, true));
		_depth--;

		return s.toString();
	}

	@Override
	public Object visit(ForEach forEach, Object isStatement)
	{
		StringBuffer s = new StringBuffer();
		
		ident(s);
		s.append(String.format("for (%s in %s) ", forEach.getItem().accept(this, false), forEach.getCollection().accept(this, false)));
		_depth++;
		s.append(forEach.getStatementsBlock().accept(this, true));
		_depth--;

		return s.toString();
	}

	@Override
	public Object visit(Continue continueStatement, Object isStatement)
	{
		return identString("continue;");
	}

	@Override
	public Object visit(Break breakStatement, Object isStatement)
	{
		return identString("break;");
	}

	@Override
	public Object visit(Switch switchStatement, Object isStatement)
	{
		StringBuffer s = new StringBuffer();
		
		ident(s);
		s.append(String.format("switch (%s) {", switchStatement.getExpression().accept(this, false)));
		_depth++;
		s.append(listJoin(switchStatement.getCasesOps(), false));
		_depth--;
		ident(s);
		s.append("}");
		
		return s.toString();
	}
	
	@Override
	public Object visit(CaseBlock caseBlock, Object isStatement)
	{
		StringBuffer s = new StringBuffer();

		s.append(listJoin(caseBlock.getCases(), false) + " ");
		_depth++;
		s.append(caseBlock.getStatementBlock().accept(this, true));
		_depth--;
		
		return s.toString();
	}
	
	@Override
	public Object visit(Case myCase, Object context)
	{
		return identString(String.format("case %s:", myCase.getCaseExpr().accept(this, context)));
	}

	@Override
	public Object visit(Default myDefault, Object context)
	{
		return identString("default:");
	}

	@Override
	public Object visit(Return returnStatement, Object isStatement)
	{
		String res;
		
		if (returnStatement.hasValue())
			res = identString(String.format("return %s;", returnStatement.getValue().accept(this, false)));
		else
			res = identString("return;");
		
		return res;
	}

	@Override
	public Object visit(If ifStatement, Object isStatement)
	{
		StringBuffer s = new StringBuffer();
		
		ident(s);
		s.append(String.format("if (%s) ", ifStatement.getCondition().accept(this, false)));
		_depth++;
		s.append(ifStatement.getStatementsBlock().accept(this, true));
		_depth--;
		
		if (ifStatement.hasElse()) {
			ident(s);
			s.append("else ");
			_depth++;
			s.append(ifStatement.getElseStatementsBlock().accept(this, true)); // todo
			_depth--;
		}
		
		return s.toString();
	}

	@Override
	public Object visit(StatementsBlock stmtBlock, Object isStatement)
	{
		StringBuffer s = new StringBuffer();
		
		s.append("{");
		s.append(listJoin(stmtBlock.getStatements(), true));
		_depth--;
		ident(s);
		_depth++;
		s.append("}");
		
		return s.toString();
	}

	@Override
	public Object visit(Assignment assignment, Object isStatement)
	{
		String res = String.format("%s = %s", assignment.getLeftHandSide().accept(this, false), assignment.getExpr().accept(this, false));
		
		if (isTrue(isStatement))
			res = identString(res + ";");

		return res;
	}

	@Override
	public Object visit(CompoundAssignment assignment, Object isStatement)
	{
		String res = String.format("%s %s= %s", assignment.getLeftHandSide().accept(this, false), assignment.getCompoundOp().getToken(), assignment.getExpr().accept(this, false));
		
		if (isTrue(isStatement))
			res = identString(res + ";");
		
		return res;
	}

	@Override
	public Object visit(Call call, Object isStatement)
	{
		String res = String.format("%s(%s)", call.getBase().accept(this, false), listJoin(call.getParams(), false, ", "));
		
		if (isTrue(isStatement))
			res = identString(res + ";");
		
		return res;
	}

	@Override
	public Object visit(ArrayExpression arrayExp, Object isStatement)
	{
		String res = String.format("[%s]", listJoin(arrayExp.getItemsList(), false, ", "));
		
		if (isTrue(isStatement))
			res = identString(res + ";");

		return res;
	}

	@Override
	public Object visit(FunctionExpression functionExpression, Object isStatement)
	{
		StringBuffer s = new StringBuffer();
		
		ident(s);
		s.append(String.format("function (%s) ", listJoin(functionExpression.getFormals(), false, ", ")));
		_depth++;
		s.append(functionExpression.getStatementsBlock().accept(this, true));
		_depth--;

		return s.toString();
	}

	@Override
	public Object visit(Identifier id, Object isStatement)
	{
		String res = id.getName();
		
		if (isTrue(isStatement))
			res = identString(res + ";");
		
		return res;
	}

	@Override
	public Object visit(MemberExpression memberExpr, Object isStatement)
	{
		String res;
		
		if (memberExpr.getLocation() instanceof Identifier)
			res = String.format("%s.%s", memberExpr.getBase().accept(this, false), memberExpr.getLocation().accept(this, false));
		else
			res = String.format("%s[%s]", memberExpr.getBase().accept(this, false), memberExpr.getLocation().accept(this, false));
		
		if (isTrue(isStatement))
			res = identString(res + ";");
		return res;
	}

	@Override
	public Object visit(ObjectExpression objExpr, Object isStatement)
	{
		StringBuffer s = new StringBuffer();
		
		if (isTrue(isStatement))
			ident(s);

		s.append("{");
		_depth++;
		for (Entry<ObjectKeys, AbsExpression> entry : objExpr.getMap().entrySet()) {
			ident(s);
			s.append(String.format("%s: %s", entry.getKey().accept(this, false), entry.getValue().accept(this, false)));
		}
		_depth--;
		ident(s);
		s.append("}");
		
		if (isTrue(isStatement))
			s.append(";");
		
		return s.toString();
	}

	@Override
	public Object visit(This thisExpr, Object isStatement)
	{
		String res = "this";
		
		if (isTrue(isStatement))
			res = identString(res + ";");
		
		return res;
	}

	@Override
	public Object visit(UnaryOp unaryOp, Object isStatement)
	{
		String res = "(" + unaryOp.getOperator().formatOp((String)unaryOp.getOperand().accept(this, false)) + ")";
		
		if (isTrue(isStatement))
			res = identString(res + ";");
		
		return res;
	}

	@Override
	public Object visit(BinaryOp binaryOp, Object isStatement)
	{
		String res = "(" + binaryOp.getOperator().formatOp((String)binaryOp.getFirstOperand().accept(this, false), (String)binaryOp.getSecondOperand().accept(this, false)) + ")";
		
		if (isTrue(isStatement))
			res = identString(res + ";");
		
		return res;
	}

	@Override
	public Object visit(TrinaryOp trinaryOp, Object isStatement)
	{
		String res = "(" + trinaryOp.getOperator().formatOp((String)trinaryOp.getFirstOperand().accept(this, false), (String)trinaryOp.getSecondOperand().accept(this, false), (String)trinaryOp.getThirdOperand().accept(this, false)) + ")";
		
		if (isTrue(isStatement))
			res = identString(res + ";");
		
		return res;
	}
	
	@Override
	public Object visit(Literal literal, Object isStatement)
	{
		String res = literal.getType().getToken();
		
		if (isTrue(isStatement))
			res = identString(res + ";");
		
		return res;
	}

	@Override
	public Object visit(LiteralString literal, Object isStatement)
	{
		String res = "\"" + StringEscapeUtils.escapeJava(literal.getValue()) + "\"";
		
		if (isTrue(isStatement))
			res = identString(res + ";");
		
		return res;
	}

	@Override
	public Object visit(LiteralNumber literal, Object isStatement)
	{
		String res = literal.getValue();
		
		if (isTrue(isStatement))
			res = identString(res + ";");
		
		return res;
	}
	
	// -------------------------------------------------
	
	private String listJoinFormat(List<? extends JSTObject> list, boolean isStatement, String format, String delimiter)
	{
		StringBuilder s = new StringBuilder();
		
		if (list.size() > 0)
		{
			s.append(String.format(format, list.get(0).accept(this, isStatement)));
			
			if (list.size() > 1) {
				for (int i=1; i<list.size() ; i++) {
					s.append(delimiter + String.format(format, list.get(i).accept(this, isStatement)));
				}
			}
		}
		
		return s.toString();
	}
	
	private String listJoin(List<? extends JSTObject> list, boolean isStatement, String delimiter)
	{
		return listJoinFormat(list, isStatement, "%s", delimiter);
	}
	
	private String listJoin(List<? extends JSTObject> list, boolean isStatement)
	{
		return listJoinFormat(list, isStatement, "%s", "");
	}
	
	private void ident(StringBuffer s)
	{
		s.append(EOL);
		for (int i=0; i<_depth ; i++)
			s.append(IDENT);
	}
	
	private String identString(String str)
	{
		StringBuffer s = new StringBuffer();
	
		ident(s);
		s.append(str);
		
		return s.toString();
	}
	
	private boolean isTrue(Object booleanValue)
	{
		return ((boolean)booleanValue == true);
	}
}
