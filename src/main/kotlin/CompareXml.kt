import org.w3c.dom.Attr
import org.xmlunit.builder.DiffBuilder
import org.xmlunit.builder.Input
import org.xmlunit.diff.DefaultNodeMatcher
import org.xmlunit.diff.Diff
import org.xmlunit.diff.ElementSelectors
import java.nio.file.Path
import java.nio.file.Paths

data class CompareOptions(val verbose: Boolean = false)

fun compareXmlFromCli(
    leftXml: String,
    rightXml: String,
    ignoreListStr: String,
    opts: CompareOptions = CompareOptions(verbose = false)
): String {
    val cwd = Paths.get("").toAbsolutePath().toString();
    val leftPath = Paths.get(cwd, leftXml)
    val rightPath = Paths.get(cwd, rightXml)
    val ignoreList = ignoreListStr.split(",")
    return compareXmlFiles(leftPath, rightPath, ignoreList, opts)
}

fun compareXmlFiles(leftXmlPath: Path, rightXmlPath: Path, ignoreList: List<String>, opts: CompareOptions): String {
    val left = leftXmlPath.toFile().readText()
    val right = rightXmlPath.toFile().readText()
    return compareXmlText(left, right, ignoreList, opts)
}

fun compareXmlText(leftXml: String, rightXml: String, ignoreList: List<String>, opts: CompareOptions): String {
    val d: Diff = DiffBuilder
        .compare(Input.fromString(leftXml))
        .withTest(Input.fromString(rightXml))
        .withNodeMatcher(DefaultNodeMatcher(ElementSelectors.byNameAndText))
        .withNodeFilter { node -> !ignoreList.contains(node.nodeName) }
        .withAttributeFilter { attr -> !ignoreList.any { toIgnore -> shouldIgnoreAttr(toIgnore, attr) } }
        .ignoreComments()
        .checkForSimilar()
        .normalizeWhitespace()
        .build()

    return if (opts.verbose) d.fullDescription().orEmpty() else shortDescription(d);
}

private fun shouldIgnoreAttr(toIgnore: String, attr: Attr): Boolean {
    val split = toIgnore.split(".");
    if (split.size < 2)
        return false
    val nodeName = split.getOrNull(0)
    val attrName = split.getOrNull(1)
    return nodeName == attr.ownerElement.nodeName && attrName == attr.nodeName
}

fun shortDescription(a: Diff): String {
    val short = a.differences.map { diff ->
        """
        ${diff.comparison.type.description}: [${diff.comparison.controlDetails.value} | ${diff.comparison.testDetails.value}] (${'$'}${diff.comparison.testDetails.xPath}) 
    """.trimIndent()
    }
    return short.joinToString ("\n")
}