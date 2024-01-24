package grammar.representation
import grammar.*

class BracketPrinter : Expr.Visitor<String> {
    fun print(expr: Expr): String {
        return expr.accept(this)
    }

    override fun visitBinaryExpr(expr: Binary): String = parenthesize(expr.operator.lexeme, expr.left, expr.right )
    override fun visitGroupingExpr(expr: Grouping): String = parenthesize("group", expr.expression)
    override fun visitLiteralExpr(expr: Literal?): String = expr?.value?.toString() ?: "null"
    override fun visitUnaryExpr(expr: Unary): String = parenthesize(expr.operator.lexeme, expr.expression)

    private fun parenthesize(name: String, vararg expressions: Expr): String = buildString {
        append("(", name)
        expressions.forEach {
            append(" ", it.accept(this@BracketPrinter))
        }
        append(")")
    }
}
