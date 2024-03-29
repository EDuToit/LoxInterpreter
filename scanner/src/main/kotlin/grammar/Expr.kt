package grammar

abstract class Expr {
    interface Visitor<R> {
//        fun visitAssignExpr(expr: Assign?): R
        fun visitBinaryExpr(expr: Binary): R
//        fun visitCallExpr(expr: Call?): R
//        fun visitGetExpr(expr: Get?): R
        fun visitGroupingExpr(expr: Grouping): R
        fun visitLiteralExpr(expr: Literal?): R
//        fun visitLogicalExpr(expr: Logical?): R
//        fun visitSetExpr(expr: Set<*>?): R
//        fun visitSuperExpr(expr: Super?): R
//        fun visitThisExpr(expr: This?): R
        fun visitUnaryExpr(expr: Unary): R
//        fun visitVariableExpr(expr: Variable?): R
    }
    abstract fun <R> accept(visitor: Visitor<R>): R
    data class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitBinaryExpr(this)
    }

    data class Grouping(val expression: Expr): Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitGroupingExpr(this)
    }

    data class Literal(val value: Any?): Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitLiteralExpr(this)
    }

    data class Unary(val operator: Token, val expression: Expr): Expr() {
        override fun <R> accept(visitor: Visitor<R>): R = visitor.visitUnaryExpr(this)
    }
}
