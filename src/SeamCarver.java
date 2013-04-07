import java.awt.*;

/**
 * User: huangcd (huangcd.thu@gmail.com)
 * Date: 4/7/13
 * Time: 3:06 PM
 */
public class SeamCarver {
    private static final double BORDER_ENERGY = 195075.0;
    private int[][] colors;
    private double[][] energies;
    private int width;
    private int height;

    public SeamCarver(Picture picture) {
        width = picture.width();
        height = picture.height();
        colors = new int[width][height];
        energies = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                colors[x][y] = parseColor(picture.get(x, y));
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                energies[x][y] = calcEnergy(x, y);
            }
        }
    }

    private int getRed(int color) {
        return color & 255;
    }

    private int getGreen(int color) {
        return (color >> 8) & 255;
    }

    private int getBlue(int color) {
        return color >> 16;
    }

    private int parseColor(Color color) {
        return (color.getBlue() << 16) | (color.getGreen() << 8) | (color.getRed());
    }

    private Color toColor(int color) {
        return new Color(getRed(color), getGreen(color), getBlue(color));
    }

    /**
     * @return current picture
     */
    public Picture picture() {
        Picture pic = new Picture(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pic.set(x, y, toColor(colors[x][y]));
            }
        }
        return pic;
    }

    /**
     * @return width of current picture
     */
    public int width() {
        return width;
    }

    /**
     * @return height of current picture
     */
    public int height() {
        return height;
    }

    private boolean isBorder(int x, int y) {
        if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
            return true;
        }
        return false;
    }

    private void validate(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw new IndexOutOfBoundsException(String.format("Image range = "
                    + "(%d, %d), input is (%d, %d)", width, height, x, y));
        }
    }

    private double calcEnergy(int x, int y) {
        if (isBorder(x, y)) {
            return BORDER_ENERGY;
        }
        int rx = Math.abs(getRed(colors[x - 1][y]) - getRed(colors[x + 1][y]));
        int bx = Math.abs(getGreen(colors[x - 1][y]) - getGreen(colors[x + 1][y]));
        int gx = Math.abs(getBlue(colors[x - 1][y]) - getBlue(colors[x + 1][y]));

        int ry = Math.abs(getRed(colors[x][y - 1]) - getRed(colors[x][y + 1]));
        int by = Math.abs(getGreen(colors[x][y - 1]) - getGreen(colors[x][y + 1]));
        int gy = Math.abs(getBlue(colors[x][y - 1]) - getBlue(colors[x][y + 1]));

        int energy = rx * rx + bx * bx + gx * gx + ry * ry + by * by + gy * gy;
        return energy;
    }

    /**
     * @return energy of pixel at column x and row y
     */
    public double energy(int x, int y) {
        return energies[x][y];
    }

    /**
     * @return sequence of indices for horizontal seam
     */
    public int[] findHorizontalSeam() {
        int[][] edgeTo = new int[width][height];
        double[][] total = new double[width][height];
        for (int x1 = 0; x1 < width; x1++) {
            for (int y1 = 0; y1 < height; y1++) {
                total[x1][y1] = Double.MAX_VALUE;
            }
        }
        for (int y = 0; y < height; y++) {
            total[0][y] = BORDER_ENERGY;
        }
        for (int x = 0, max = width - 1; x < max; x++) {
            for (int y = 0; y < height; y++) {
                // right top
                if (y > 0) {
                    if (total[x + 1][y - 1] > total[x][y] + energies[x + 1][y - 1]) {
                        edgeTo[x + 1][y - 1] = y;
                        total[x + 1][y - 1] = total[x][y] + energies[x + 1][y - 1];
                    }
                }
                // right
                if (total[x + 1][y] > total[x][y] + energies[x + 1][y]) {
                    edgeTo[x + 1][y] = y;
                    total[x + 1][y] = total[x][y] + energies[x + 1][y];
                }
                // right bottom
                if (y < height - 1) {
                    if (total[x + 1][y + 1] > total[x][y] + energies[x + 1][y + 1]) {
                        edgeTo[x + 1][y + 1] = y;
                        total[x + 1][y + 1] = total[x][y] + energies[x + 1][y + 1];
                    }
                }
            }
        }
        int[] seam = new int[width];
        int minYIndex = -1;
        double minValue = Double.MAX_VALUE;
        for (int y = 0, x = width - 1; y < height; y++) {
            if (minValue > total[x][y]) {
                minValue = total[x][y];
                minYIndex = y;
            }
        }
        seam[width - 1] = minYIndex;
        for (int x = width - 2; x >= 0; x--) {
            seam[x] = edgeTo[x + 1][seam[x + 1]];
        }
        return seam;
    }

    /**
     * @return sequence of indices for vertical seam
     */
    public int[] findVerticalSeam() {
        int[][] edgeTo = new int[width][height];
        double[][] total = new double[width][height];
        for (int x1 = 0; x1 < width; x1++) {
            for (int y1 = 0; y1 < height; y1++) {
                total[x1][y1] = Double.MAX_VALUE;
            }
        }
        for (int x = 0; x < width; x++) {
            total[x][0] = BORDER_ENERGY;
        }
        for (int y = 0, max = height - 1; y < max; y++) {
            for (int x = 0; x < width; x++) {
                // bottom left
                if (x > 0) {
                    if (total[x - 1][y + 1] > total[x][y] + energies[x - 1][y + 1]) {
                        edgeTo[x - 1][y + 1] = x;
                        total[x - 1][y + 1] = total[x][y] + energies[x - 1][y + 1];
                    }
                }
                // bottom
                if (total[x][y + 1] > total[x][y] + energies[x][y + 1]) {
                    edgeTo[x][y + 1] = x;
                    total[x][y + 1] = total[x][y] + energies[x][y + 1];
                }
                // bottom right
                if (x < width - 1) {
                    if (total[x + 1][y + 1] > total[x][y] + energies[x + 1][y + 1]) {
                        edgeTo[x + 1][y + 1] = x;
                        total[x + 1][y + 1] = total[x][y] + energies[x + 1][y + 1];
                    }
                }
            }
        }
        int[] seam = new int[height];
        int minXIndex = -1;
        double minValue = Double.MAX_VALUE;
        for (int x = 0, y = height - 1; x < width; x++) {
            if (minValue > total[x][y]) {
                minValue = total[x][y];
                minXIndex = x;
            }
        }
        seam[height - 1] = minXIndex;
        for (int y = height - 2; y >= 0; y--) {
            seam[y] = edgeTo[seam[y + 1]][y + 1];
        }
        return seam;
    }

    /**
     * remove horizontal seam from picture
     */
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != width || height == 1 || width == 1) {
            throw new IllegalArgumentException("Seam not validated");
        }
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("Seam not validated");
            }
        }
        for (int x = 0; x < width; x++) {
            int y = seam[x];
            validate(x, y);
            for (int yy = y, max = height - 1; yy < max; yy++) {
                colors[x][yy] = colors[x][yy + 1];
            }
        }
        for (int x = 0; x < width; x++) {
            int y = seam[x];
            for (int yy = y, max = height - 1; yy < max; yy++) {
                energies[x][yy] = calcEnergy(x, yy);
            }
        }
        height--;
    }

    /**
     * remove vertical seam from picture
     */
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height || height == 1 || width == 1) {
            throw new IllegalArgumentException("Seam not validated");
        }
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("Seam not validated");
            }
        }
        for (int y = 0; y < height; y++) {
            int x = seam[y];
            validate(x, y);
            for (int xx = x, max = width - 1; xx < max; xx++) {
                colors[xx][y] = colors[xx + 1][y];
            }
        }
        for (int y = 0; y < height; y++) {
            int x = seam[y];
            for (int xx = x, max = width - 1; xx < max; xx++) {
                energies[xx][y] = calcEnergy(xx, y);
            }
        }
        width--;
    }
}

