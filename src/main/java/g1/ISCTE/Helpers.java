package g1.ISCTE;

import java.util.Optional;

/**
 * Helper class.
 */
public class Helpers {

    /**
     * Gets, if possible, the extension of a given file.
     *
     * @param filename the name of the file.
     * @return an optional.
     */
    public static Optional<String> getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

}
