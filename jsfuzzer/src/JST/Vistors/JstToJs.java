package JST.Vistors;

import java.util.List;
import java.util.Map.Entry;

import JST.*;
import JST.Switch.CaseBlock;
import JST.VarDecleration.VarDeclerator;
import JST.Interfaces.ObjectKeys;
import JST.Interfaces.Visitor;

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
		return (String) visit(program);
	}
	
	// --------------------------------------------
	
	@Override
	public Object visit(Program program)
	{
		return listJoin(program.getStatements());
	}

	@Override
	public Object visit(FunctionDefinition functionDefinition)
	{
		StringBuffer s = new StringBuffer();
		
		ident(s);
		s.append(String.format("function %s(%s) {", functionDefinition.getId().accept(this), listJoin(functionDefinition.getFormals(), ", ")));
		_depth++;
		s.append(listJoin(functionDefinition.getStatements()));
		_depth--;
		ident(s);
		s.append("}");

		return s.toString();
	}

	@Override
	public Object visit(VarDecleration varDefinition)
	{
		return identString(String.format("var %s;", listJoin(varDefinition.getDecleratorList(), ", ")));
	}
	
	@Override
	public Object visit(VarDeclerator varDeclerator)
	{
		if (varDeclerator.hasInit())
			return String.format("%s = %s", varDeclerator.getId().accept(this), varDeclerator.getInit().accept(this));
		else
			return varDeclerator.getId().accept(this);
	}

	@Override
	public Object visit(While whileStatement)
	{
		StringBuffer s = new StringBuffer();
		
		ident(s);
		s.append(String.format("while (%s)", whileStatement.getCondition().accept(this)));
		_depth++;
		s.append(whileStatement.getOperation().accept(this));
		_depth--;
		
		return s.toString();
	}

	@Override
	public Object visit(DoWhile doWhile)
	{
		StringBuffer s = new StringBuffer();
		
		ident(s);
		s.append("do");
		_depth++;
		s.append(doWhile.getOperation().accept(this));
		_depth--;
		ident(s);
		s.append(String.format("while (%s);",doWhile.getCondition().accept(this)));
		
		return s.toString();
	}

	@Override
	public Object visit(For forStatement)
	{
		StringBuffer s = new StringBuffer();
		
		ident(s);
		s.append(String.format("for (%s ; %s ; %s)", forStatement.getInitStatement().accept(this), forStatement.getConditionExpression().accept(this), forStatement.getStepExpression().accept(this)));
		_depth++;
		s.append(forStatement.getOperation().accept(this));
		_depth--;
		
		return s.toString();
	}

	@Override
	public Object visit(ForEach forEach)
	{
		StringBuffer s = new StringBuffer();
		
		ident(s);
		s.append(String.format("for (%s in %s)", forEach.getItem().accept(this), forEach.getCollection().accept(this)));
		_depth++;
		s.append(forEach.getOperation().accept(this));
		_depth--;
		
		return s.toString();
	}

	@Override
	public Object visit(Continue continueStatement)
	{
		return identString("continue;");
	}

	@Override
	public Object visit(Break breakStatement)
	{
		return identString("break;");
	}

	@Override
	public Object visit(Switch switchStatement)
	{
		StringBuffer s = new StringBuffer();
		
		ident(s);
		s.append(String.format("switch (%s) {", switchStatement.getExpression().accept(this)));
		_depth++;
		s.append(listJoin(switchStatement.getCasesOps()));
		_depth--;
		ident(s);
		s.append("}");
		
		return s.toString();
	}
	
	@Override
	public Object visit(CaseBlock caseBlock)
	{
		StringBuffer s = new StringBuffer();
		
		ident(s);
		s.append(listJoinFormat(caseBlock.getCases(), "case %s:", " "));
		_depth++;
		s.append(listJoin(caseBlock.getStatements()));
		_depth--;
		
		return s.toString();
	}

	@Override
	public Object visit(Return returnStatement)
	{
		if (returnStatement.hasValue())
			return identString(String.format("return %s;", returnStatement.getValue().accept(this)));
		else
			return identString("return;");
	}

	@Override
	public Object visit(If ifStatement)
	{
		StringBuffer s = new StringBuffer();
		
		ident(s);
		s.append(String.format("if (%s)", ifStatement.getCondition().accept(this)));
		_depth++;
		s.append(ifStatement.getOperation().accept(this));
		_depth--;
		
		if (ifStatement.hasElse()) {
			ident(s);
			s.append("else");
			_depth++;
			s.append(ifStatement.getElseOperation().accept(this));
			_depth--;
		}
		
		return s.toString();
	}

	@Override
	public Object visit(StatementsBlock stmtBlock)
	{
		StringBuffer s = new StringBuffer();
		
		s.append(" {");
		s.append(listJoin(stmtBlock.getStatements()));
		_depth--;
		ident(s);
		_depth++;
		s.append("}");
		
		return s.toString();
	}

	@Override
	public Object visit(Assignment assignment)
	{
		return identString(String.format("%s = %s;", assignment.getLeftHandSide().accept(this), assignment.getExpr().accept(this)));
	}

	@Override
	public Object visit(CompoundAssignment assignment)
	{
		return identString(String.format("%s %s= %s;", assignment.getLeftHandSide().accept(this), assignment.getCompoundOp().getToken(), assignment.getExpr().accept(this)));
	}

	@Override
	public Object visit(Call call)
	{
		return String.format("%s(%s)", call.getBase().accept(this), listJoin(call.getParams(), ", "));
	}

	@Override
	public Object visit(ArrayExpression arrayExp)
	{
		return String.format("[%s]", listJoin(arrayExp.getItemsList(), ", "));
	}

	@Override
	public Object visit(FunctionExpression functionExpression)
	{
		StringBuffer s = new StringBuffer();
		
		ident(s);
		s.append(String.format("function (%s) {", listJoin(functionExpression.getFormals(), ", ")));
		_depth++;
		s.append(listJoin(functionExpression.getStatements()));
		_depth--;
		ident(s);
		s.append("}");

		return s.toString();
	}

	@Override
	public Object visit(Identifier id)
	{
		return id.getName();
	}

	@Override
	public Object visit(MemberExpression memberExpr)
	{
		if (memberExpr.getLocation() instanceof Identifier)
			return String.format("%s.%s", memberExpr.getBase().accept(this), memberExpr.getLocation().accept(this));
		else
			return String.format("%s[%s]", memberExpr.getBase().accept(this), memberExpr.getLocation().accept(this));
	}

	@Override
	public Object visit(ObjectExpression objExpr)
	{
		StringBuffer s = new StringBuffer();

		s.append("{");
		_depth++;
		for (Entry<ObjectKeys, AbsExpression> entry : objExpr.getMap().entrySet()) {
			ident(s);
			s.append(String.format("%s: %s", entry.getKey().accept(this), entry.getValue().accept(this)));
		}
		_depth--;
		ident(s);
		s.append("}");
		
		return s.toString();
	}

	@Override
	public Object visit(This thisExpr)
	{
		return "this";
	}

	@Override
	public Object visit(UnaryOp unaryOp)
	{
		return "(" + unaryOp.getOperator().formatOp((String)unaryOp.getOperand().accept(this)) + ")";
	}

	@Override
	public Object visit(BinaryOp binaryOp)
	{
		return "(" + binaryOp.getOperator().formatOp((String)binaryOp.getFirstOperand().accept(this), (String)binaryOp.getSecondOperand().accept(this)) + ")";
	}

	@Override
	public Object visit(TrinaryOp trinaryOp)
	{
		return "(" + trinaryOp.getOperator().formatOp((String)trinaryOp.getFirstOperand().accept(this), (String)trinaryOp.getSecondOperand().accept(this), (String)trinaryOp.getThirdOperand().accept(this)) + ")";
	}
	
	@Override
	public Object visit(Literal literal)
	{
		return literal.getType().getToken();
	}

	@Override
	public Object visit(LiteralString literal)
	{
		return literal.getValue();
	}

	@Override
	public Object visit(LiteralNumber literal)
	{
		return literal.getValue();
	}
	
	// -------------------------------------------------
	
	private String listJoinFormat(List<? extends JSTNode> list, String format, String delimater)
	{
		StringBuilder s = new StringBuilder();
		
		if (list.size() > 0)
		{
			s.append(String.format(format, list.get(0).accept(this)));
			
			if (list.size() > 1) {
				for (int i=1; i<list.size() ; i++) {
					s.append(delimater + String.format(format, list.get(i).accept(this)));
				}
			}
		}
		
		return s.toString();
	}
	
	private String listJoin(List<? extends JSTNode> list, String delimater)
	{
		return listJoinFormat(list, "%s", delimater);
	}
	
	private String listJoin(List<? extends JSTNode> list)
	{
		return listJoinFormat(list, "%s", "");
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
}
