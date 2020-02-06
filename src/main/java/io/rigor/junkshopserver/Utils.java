package io.rigor.junkshopserver;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Utils {
  public static String roundToTwo(Double dd) {
    DecimalFormat format = new DecimalFormat("#.##");
    format.setRoundingMode(RoundingMode.CEILING);
    return format.format(dd);
  }
}
