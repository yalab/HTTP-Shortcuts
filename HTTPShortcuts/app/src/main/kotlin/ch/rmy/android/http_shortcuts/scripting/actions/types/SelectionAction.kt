package ch.rmy.android.http_shortcuts.scripting.actions.types

import ch.rmy.android.http_shortcuts.dialogs.DialogBuilder
import ch.rmy.android.http_shortcuts.extensions.mapFor
import ch.rmy.android.http_shortcuts.extensions.takeUnlessEmpty
import ch.rmy.android.http_shortcuts.extensions.toListOfStrings
import ch.rmy.android.http_shortcuts.scripting.ExecutionContext
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SelectionAction(private val jsonData: String) : BaseAction() {

    override fun executeForValue(executionContext: ExecutionContext): Single<String> {
        val options = parseData(jsonData);
        return if (options.isNotEmpty()) {
            Single.create<String> { emitter ->
                DialogBuilder(executionContext.context)
                    .mapFor(options.entries) { builder, entry ->
                        builder.item(name = entry.value) {
                            emitter.onSuccess("-${entry.key}")
                        }
                    }
                    .dismissListener { emitter.onSuccess("") }
                    .show()
            }
                .subscribeOn(AndroidSchedulers.mainThread())
                .map {
                    it.takeUnlessEmpty()
                        ?.removePrefix("-")
                        ?: NO_RESULT
                }
        } else {
            Single.just(NO_RESULT)
        }
    }

    private fun parseData(data: String): Map<String, String> {
        try {
            val obj = JSONObject(data)
            return obj.keys().asSequence().associateWith { key -> obj.getString(key) }
        } catch (e: JSONException) {
            val array = JSONArray(data)
            return array.toListOfStrings()
                .associateWith { it }
        }
    }

}