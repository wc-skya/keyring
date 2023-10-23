package xy.lib;

public class Random {

  public Random() {
    init();
  }

  public Random(String seed) {
    init(seed);
  }

  public void init() {
    init(System.nanoTime() + "." + System.currentTimeMillis());
  }

  public void init(String seed) {
    byte[] buf = seed.getBytes();
    for (int i = 0; i < 30; x[i++] = 0);
    for (int i, j = 0; j < 150; j++) {
      x[i = j % 30] <<= 8;
      if (j < buf.length) x[i] |= buf[j] & 0xff;
    }
    long f = 0;
    for (int i = 0; i < 30; i++) f += x[i] = g[i].next(x[i]);
    for (int i = 0; i < 30; i++) x[i] = g[i].next((x[i] + f) % g[i].m);
  }

  public double next() {
    int i = (int)((x[29] = g[29].next(x[29])) % 29);
    return (double)(x[i] = g[i].next(x[i])) / g[i].m;
  }

  public long next(long range) {
    return (long)(next() * range);
  }

  private static class LCG {
    LCG(int k, int p, int q) {
      m = p * (long)p * p;
      a = k * p + 1;
      c = q;
    }
    long next(long x) {
      return (a * x + c) % m;
    }
    final long m;
    final int a, c;
  }

  private long[] x = new long [30];
  private static final LCG[] g = {
    new LCG(131, 15727, 15991), new LCG(135, 15731, 16001), new LCG(133, 15733, 16007),
    new LCG(132, 15737, 16033), new LCG(138, 15739, 16057), new LCG(136, 15749, 16061),
    new LCG(134, 15761, 16063), new LCG(132, 15767, 16067), new LCG(134, 15773, 16069),
    new LCG(148, 15787, 16073), new LCG(131, 15791, 16087), new LCG(139, 15797, 16091),
    new LCG(136, 15803, 16097), new LCG(133, 15809, 16103), new LCG(136, 15817, 16111),
    new LCG(130, 15823, 16127), new LCG(135, 15859, 16139), new LCG(138, 15877, 16141),
    new LCG(130, 15881, 16183), new LCG(130, 15887, 16187), new LCG(139, 15889, 16189),
    new LCG(141, 15901, 16193), new LCG(134, 15907, 16217), new LCG(132, 15913, 16223),
    new LCG(136, 15919, 16229), new LCG(135, 15923, 16231), new LCG(135, 15937, 16249),
    new LCG(142, 15959, 16253), new LCG(135, 15971, 16267), new LCG(136, 15973, 16273)};
}
