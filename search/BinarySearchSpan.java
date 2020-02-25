package search;

public class BinarySearchSpan {
    // pre: args.length > 0
    //      forall i = 0..(args.length - 1): args[i] is Integer
    // post: a[left - 1] > x
    //       a[left + len] < x
    //       forall i = left..(len - 1): a[i] = x
    public static void main(final String... args) {
        assert args.length > 0;

        final int x = Integer.parseInt(args[0]);
        final int[] a = new int[args.length - 1];

        for (int i = 0; i < a.length; ++i) {
            a[i] = Integer.parseInt(args[i + 1]);
        }

        int left = BinarySearchRecursive(x, a, true);
        int len = BinarySearchIterative(x, a, false) - left;

        System.out.println(left + " " + len);
    }

    // pre: forall i = 1..(a.length - 1): a[i - 1] >= a[i]
    // post: R = a.length || a[R] < x || (a[R] = x && equals)
    //       R = 0 || a[R - 1] > x || (a[R - 1] = x && !equals)
    public static int BinarySearchIterative(final int x, final int[] a, boolean equals) {
        int left = -1;
        // left = -1
        int right = a.length;
        // right = a.length >= 0 > left = -1 => right > left

        // inv: right > left
        //      right = a.length || a[right] < x || (a[right] = x && equals)
        //      left = -1 || a[left] > x || (a[left] = x && !equals)
        //      (right' - left') * 2 <= right - left
        while (left + 1 < right) {
            // a != null
            // left + 1 < right
            int mid = (left + right) / 2;
            // left < right - 1 => left + right < 2 * right - 1 => mid < right
            // right > left + 1 => left + right > 2 * left  + 1 => mid > left
            // left < mid < right
            // (right - mid) * 2 <= right - left
            //  (mid - left) * 2 <= right - left
            if (a[mid] < x || (equals && a[mid] == x)) {
                // a[mid] < x || (a[mid] = x && equals)
                right = mid;
                // left' = left
                // right' = mid
                // a[right'] < x || (a[right'] = x && equals)
            } else {
                // a[mid] > x || (a[mid] = x && !equals)
                left = mid;
                // left' = mid
                // right' = right
                // a[left'] > x || (a[left] = x && !equals)
            }
            // (right' - left') * 2 <= right - left
        }
        // left + 1 == right
        // right = a.length || a[right] < x || (a[right] = x && equals)
        return right;
    }

    // pre: forall i = 1..(a.length - 1): a[i - 1] >= a[i]
    //      right > left
    //      right = a.length || a[right] < x || (a[right] = x && equals)
    //      left = -1 || a[left] > x || (a[left] = x && !equals)
    //      (right - left) * 2 <= prevRight - prevLeft
    // post: R = a.length || a[R] < x || (a[R] = x && equals)
    //       R = 0 || a[R - 1] > x || (a[R - 1] = x && !equals)
    private static int BinarySearchRecursive(final int x, final int[] a, final int left, final int right, final boolean equals) {
        if (left + 1 == right) {
            // left + 1 == right
            // right = a.length || a[right] < x || (a[right] = x && equals)
            return right;
        }
        // a != null
        // left + 1 < right
        int mid = (left + right) / 2;
        // left < right - 1 => left + right < 2 * right - 1 => mid < right
        // right > left + 1 => left + right > 2 * left  + 1 => mid > left
        // left < mid < right
        // (right - mid) * 2 <= right - left
        //  (mid - left) * 2 <= right - left
        if (a[mid] < x || (equals && a[mid] == x)) {
            // a[mid] < x || (a[mid] = x && equals)
            return BinarySearchRecursive(x, a, left, mid, equals);
        }
        // a[mid] > x || (a[mid] = x && !equals)
        return BinarySearchRecursive(x, a, mid, right, equals);
    }

    // pre: forall i = 1..(a.length - 1): a[i - 1] >= a[i]
    // post: R = a.length || a[R] < x || (a[R] = x && equals)
    //       R = 0 || a[R - 1] > x || (a[R - 1] = x && !equals)
    public static int BinarySearchRecursive(final int x, final int[] a, final boolean equals) {
        return BinarySearchRecursive(x, a, -1, a.length, equals);
    }
}