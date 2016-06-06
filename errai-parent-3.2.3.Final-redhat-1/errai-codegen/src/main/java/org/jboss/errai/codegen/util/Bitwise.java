package org.jboss.errai.codegen.util;

import org.jboss.errai.codegen.BitwiseOperator;
import org.jboss.errai.codegen.Expression;
import org.jboss.errai.codegen.Statement;
import org.jboss.errai.codegen.builder.impl.BitwiseExpressionBuilder;
import org.jboss.errai.common.client.api.Assert;

/**
 * @author Mike Brock
 */
public class Bitwise {
  public static Expression<BitwiseOperator> expr(final Object lhs, final BitwiseOperator operator, final Object rhs) {
    return BitwiseExpressionBuilder.create(lhs, operator, rhs);
  }

  public static Expression<BitwiseOperator> expr(final BitwiseOperator operator, final Object rhs) {
    return BitwiseExpressionBuilder.create(operator, rhs);
  }

  public static Expression<BitwiseOperator> expr(final Statement lhs) {
    return BitwiseExpressionBuilder.create(lhs);
  }

  public static Expression<BitwiseOperator> or(final Object lhs, final Object... rhs) {
    return append(lhs, BitwiseOperator.Or, rhs);
  }

  public static Expression<BitwiseOperator> and(final Object lhs, final Object... rhs) {
    return append(lhs, BitwiseOperator.And, rhs);
  }

  public static Expression<BitwiseOperator> xor(final Object lhs, final Object... rhs) {
    return append(lhs, BitwiseOperator.Xor, rhs);
  }

  public static Expression<BitwiseOperator> shiftRight(final Object lhs, final Object... rhs) {
    return append(lhs, BitwiseOperator.ShiftRight, rhs);
  }

  public static Expression<BitwiseOperator> unsignedShiftRight(final Object lhs, final Object... rhs) {
    return append(lhs, BitwiseOperator.UnsignedShiftRight, rhs);
  }

  public static Expression<BitwiseOperator> shiftLeft(final Object lhs, final Object... rhs) {
    return append(lhs, BitwiseOperator.ShiftLeft, rhs);
  }

  private static Expression<BitwiseOperator> append(final Object lhs,
                                                    final BitwiseOperator operator,
                                                    final Object... rhs) {
    Assert.notNull("must specify a right hand side value", rhs);

    if (rhs.length == 0) {
      throw new AssertionError("must specifiy at least one right hand side element");
    }

    Expression<BitwiseOperator> expr = BitwiseExpressionBuilder.create(lhs, operator, rhs[0]);

    for (int i = 1; i < rhs.length; i++) {
      expr = BitwiseExpressionBuilder.createUnqualifying(expr, operator, rhs[i]);
    }

    return expr;
  }
}