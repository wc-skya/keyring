package xy.txt;

public class Text {

  public static boolean isSpace(int c) {
    return c == '\t' || c == '\n' || c == '\r' || c == ' ';
  }

  public static boolean isDigit(int c) {
    return c >= '0' && c <= '9';
  }

  public static String[] tokenize(String line, int count) {
    return tokenize(line, count, (char)0);
  }

  public static String[] tokenize(String line, int count, char del) {
    String[] list = new String [count];
    char one;
    for (int len = line.length(), i = 0, j = 0, k, l; i < count;) {
      while (j < len && (one = line.charAt(j)) != del && isSpace(one)) j++;
      for (k = l = j; k < len &&
        (del == 0 ? !isSpace(line.charAt(k)) : line.charAt(k) != del); k++)
        if (!isSpace(line.charAt(k))) l = k + 1;
      list[i++] = j < l ? line.substring(j, l) : "";
      if ((j = k) < len) j++;
    }
    return list;
  }
}
