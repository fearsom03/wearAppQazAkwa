package kz.evilteamgenius.firstapp.models

class Currency() {
    lateinit var title: String
    lateinit var pubDate: String
    lateinit var description: String
    lateinit var index: String
    lateinit var change: String

    constructor(
        title: String
        , pubDate: String
        , description: String
        , index: String?
        , change: String
    ) : this() {
        this.title = title
        this.pubDate = pubDate
        this.description = description
        this.index = ""
        index?.let {
            this.index = it
        }
        this.change = change
    }

    override fun toString(): String {
        return "Currency(title='$title', pubDate='$pubDate', description='$description', index='$index', change='$change')"
    }

}