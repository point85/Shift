package org.point85.workschedule.test;

import java.math.BigDecimal;
import java.math.MathContext;

abstract class BaseTest {
	public static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;
	
	protected static final BigDecimal DELTA3 = new BigDecimal("0.001", MATH_CONTEXT);
}
