/* This file is a part of the Template Expander project
 * by Juuxel, licensed under the MIT license.
 * Full code and license: https://github.com/Juuxel/TemplateExpander
 */
package juuxel.templateexpander

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

fun main(args: Array<String>) {
    if (args.size != 2) {
        System.err.println("Usage: templateexpander <template> <rules>")
        System.exit(1)
    }

    val templateFile = Paths.get(args[0])
    val ruleFile = Paths.get(args[1])

    val template = Files.readAllLines(templateFile).joinToString(separator = "\n")

    val rules = Properties()
    rules.load(Files.newInputStream(ruleFile))

    val replacements = rules.map { (key, value) -> Replacement(key.toString(), value.toString().split(',')) }

    if (replacements.size >= 2) {
        val (first, second) = replacements

        for (a in first.to) {
            for (b in second.to) {
                val file = Paths.get(templateFile.toString().replace(first.from, a).replace(second.from, b))
                val contents = template.replace(first.from, a).replace(second.from, b)

                Files.createFile(file)
                Files.write(file, contents.split('\n'))
            }
        }
    } else {
        val r = replacements.first()

        for (a in r.to) {
            val file = Paths.get(templateFile.toString().replace(r.from, a))
            val contents = template.replace(r.from, a)

            Files.createFile(file)
            Files.write(file, contents.lines())
        }
    }
}

data class Replacement(val from: String, val to: List<String>)
