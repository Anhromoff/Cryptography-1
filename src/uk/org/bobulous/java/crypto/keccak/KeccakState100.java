package uk.org.bobulous.java.crypto.keccak;

final class KeccakState100 extends KeccakShortState {

    /*
     * The length in bits of each "lane" within the Keccak permutation state
     * array.
     */
    private static final byte LANE_LENGTH = 4;

    private static final int LANE_MASK = 0xf;

    private static final byte NUMBER_OF_ROUNDS_PER_PERMUTATION = 16;

    private static final int[] ROUND_CONSTANTS_FOR_WIDTH_100;

    static {
        ROUND_CONSTANTS_FOR_WIDTH_100 = new int[]{
                1,
                2,
                10,
                0,
                11,
                1,
                1,
                9,
                10,
                8,
                9,
                10,
                11,
                11,
                9,
                3
        };
    }

    private static final byte[][] ROTATION_CONSTANTS_FOR_WIDTH_200;

    static {
        byte[][] rotOffsets = new byte[5][5];
        rotOffsets[0] = new byte[]{
                (byte) 0,
                (byte) 0,
                (byte) 3,
                (byte) 1,
                (byte) 2};
        rotOffsets[1] = new byte[]{
                (byte) 1,
                (byte) 0,
                (byte) 2,
                (byte) 1,
                (byte) 2};
        rotOffsets[2] = new byte[]{
                (byte) 2,
                (byte) 2,
                (byte) 3,
                (byte) 3,
                (byte) 1};
        rotOffsets[3] = new byte[]{
                (byte) 0,
                (byte) 3,
                (byte) 1,
                (byte) 1,
                (byte) 0};
        rotOffsets[4] = new byte[]{
                (byte) 3,
                (byte) 0,
                (byte) 3,
                (byte) 0,
                (byte) 2
        };
        ROTATION_CONSTANTS_FOR_WIDTH_200 = rotOffsets;
    }

    @Override
    final byte getLaneLengthInBits() {
        return LANE_LENGTH;
    }

    @Override
    final byte getNumberOfRoundsPerPermutation() {
        return NUMBER_OF_ROUNDS_PER_PERMUTATION;
    }

    @Override
    final int getLaneMask() {
        return LANE_MASK;
    }

    @Override
    final byte getRotationConstantForLane(int x, int y) {
        assert x >= 0 && x < 5;
        assert y >= 0 && y < 5;
        return ROTATION_CONSTANTS_FOR_WIDTH_200[x][y];
    }

    @Override
    final int getRoundConstantForRound(int roundIndex) {
        assert roundIndex >= 0 && roundIndex < NUMBER_OF_ROUNDS_PER_PERMUTATION;
        return ROUND_CONSTANTS_FOR_WIDTH_100[roundIndex];
    }
}
