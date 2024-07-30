import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class WritingInstrumentsDemo {

    public static abstract class WritingInstrument {
        protected double remainder;

        public WritingInstrument(double initialRemainder) {
            this.remainder = initialRemainder;
        }

        public abstract void write(StringBuilder sb, char[] text);

        public void erase(StringBuilder sb) {
        }

        public double getRemainder() {
            return remainder;
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + ": " + remainder + "% remaining";
        }
    }

    public static class Pen extends WritingInstrument {
        private static final double PEN_USAGE_PER_SYMBOL = 1.15;

        public Pen(double initialRemainder) {
            super(initialRemainder);
        }

        @Override
        public void write(StringBuilder sb, char[] text) {
            for (char c : text) {
                if (remainder < PEN_USAGE_PER_SYMBOL) {
                    break;
                }
                sb.append(c);
                remainder -= PEN_USAGE_PER_SYMBOL;
            }
        }
    }

    public static class Pencil extends WritingInstrument {
        private static final double PENCIL_USAGE_PER_SYMBOL = 0.95;
        private static final double PENCIL_SELF_SHARPEN_AMOUNT = 3.0;

        private int symbolsWrittenSinceLastSharpen = 0;

        public Pencil(double initialRemainder) {
            super(initialRemainder);
        }

        @Override
        public void write(StringBuilder sb, char[] text) {
            for (char c : text) {
                if (remainder < PENCIL_USAGE_PER_SYMBOL) {
                    break;
                }
                sb.append(c);
                remainder -= PENCIL_USAGE_PER_SYMBOL;
                symbolsWrittenSinceLastSharpen++;
                if (symbolsWrittenSinceLastSharpen == 20) {
                    remainder += PENCIL_SELF_SHARPEN_AMOUNT;
                    symbolsWrittenSinceLastSharpen = 0;
                }
            }
        }

        @Override
        public void erase(StringBuilder sb) {
            if (!sb.isEmpty()) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
    }

    public static class Marker extends WritingInstrument {
        private static final double MARKER_USAGE_FIRST_20 = 1.0;
        private static final double MARKER_USAGE_NEXT_20 = 1.09;
        private static final double MARKER_USAGE_AFTER_40 = 1.21;

        private int symbolsWritten = 0;

        public Marker(double initialRemainder) {
            super(initialRemainder);
        }

        @Override
        public void write(StringBuilder sb, char[] text) {
            for (char c : text) {
                double usage;
                if (symbolsWritten < 20) {
                    usage = MARKER_USAGE_FIRST_20;
                } else if (symbolsWritten < 40) {
                    usage = MARKER_USAGE_NEXT_20;
                } else {
                    usage = MARKER_USAGE_AFTER_40;
                }

                if (remainder < usage) {
                    break;
                }

                sb.append(c);
                remainder -= usage;
                symbolsWritten++;
            }
        }
    }

    public static void main(String[] args) {
        Random random = new Random();
        WritingInstrument[] instruments = new WritingInstrument[10];
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            double initialRemainder = 100.0;
            int type = random.nextInt(3);
            switch (type) {
                case 0:
                    instruments[i] = new Pen(initialRemainder);
                    break;
                case 1:
                    instruments[i] = new Pencil(initialRemainder);
                    break;
                case 2:
                    instruments[i] = new Marker(initialRemainder);
                    break;
            }
        }

        for (int i = 0; i < 10; i++) {
            for (WritingInstrument instrument : instruments) {
                char[] text = new char[random.nextInt(3) + 3];
                for (int j = 0; j < text.length; j++) {
                    text[j] = (char) (random.nextInt(26) + 'a');
                }
                instrument.write(sb, text);
                if (instrument instanceof Pencil) {
                    instrument.erase(sb);
                }
            }
        }

        Arrays.sort(instruments, Comparator.comparingDouble(WritingInstrument::getRemainder));

        for (WritingInstrument instrument : instruments) {
            System.out.println(instrument);
        }
    }
}

