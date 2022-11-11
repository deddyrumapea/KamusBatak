package com.romnan.kamusbatak.data.local

import com.romnan.kamusbatak.data.retrofit.CulturalContentApi
import com.romnan.kamusbatak.domain.model.Partuturan
import com.romnan.kamusbatak.domain.model.Umpasa
import com.romnan.kamusbatak.domain.model.UmpasaCategory
import kotlin.random.Random

class LocalCulturalContentApi : CulturalContentApi {
    private val partuturans = listOf(
        Partuturan(
            title = "Amang/Among/Bapa",
            descriptions = listOf(
                "Panggilan kepada ayah kandung, sehari-hari menggunakan kata Among atau Bapa.",
            ),
        ),
        Partuturan(
            title = "Inang/Inong/Omak",
            descriptions = listOf(
                "Panggilan kepada ibu kandung, sehari-hari menggunakan kata Inong atau Omak.",
            ),
        ),
        Partuturan(
            title = "Amang Tua/Bapa Tua",
            descriptions = listOf(
                "panggilan kita terhadap saudara laki-laki yang lebih tua dari ayah kita,",
                "panggilan kita terhadap laki-laki yang semarga dengan kita yang urutan keturunannya setingkat dengan ayah kita tetapi ayah kita lebih muda darinya,",
                "panggilan kita kepada suami dari kakak perempuan ibu kita,",
                "panggilan kita kepada suami dari inang tua kita,",
                "panggilan kita kepada bapaknya ompung kita / ompung dolinya bapak kita (amang tua mangulahi).",
            ),
        ),
        Partuturan(
            title = "Inang Tua/Nangtua/Omak Tua/Maktua",
            descriptions = listOf(
                "panggilan kita terhadap istri dari Saudara laki-laki yang lebih tua dari ayah kita,",
                "panggilan kita terhadap istri dari orang yang semarga dengan kita yang urutan keturunannya setingkat dengan ayah kita tetapi ayah kita lebih muda darinya,",
                "panggilan kita kepada kakak perempuan ibu kita,",
                "panggilan kita kepada istri dari amang tua kita,",
                "panggilan kita kepada ibunya ompung kita / ompung borunya bapak kita (inang tua mangulahi).",
            ),
        ),
        Partuturan(
            title = "Amang Uda/Bapa Uda",
            descriptions = listOf(
                "panggilan kita terhadap adik laki-laki dari ayah kita,",
                "panggilan kita terhadap laki-laki yang semarga dengan kita yang urutan keturunannya setingkat dengan ayah kita tetapi ayah kita lebih tua darinya,",
                "panggilan kita kepada suami dari adik perempuan ibu kita,",
                "panggilan kita kepada suami dari inang uda kita.",
            ),
        ),
        Partuturan(
            title = "Inang Uda/Nanguda",
            descriptions = listOf(
                "panggilan kita terhadap istri dari adik laki-laki ayah kita,",
                "panggilan kita terhadap istri dari orang yang semarga dengan kita yang urutan keturunannya setingkat dengan ayah kita tetapi ayah kita lebih tua darinya,",
                "panggilan kita kepada adik perempuan ibu kita, sering dipanggil Tante.",
                "panggilan kita kepada istri dari amang uda kita.",
            ),
        ),
        Partuturan(
            title = "Haha/Hahang/Angkang",
            descriptions = listOf(
                "panggilan kita kepada abang kandung kita maupun abang sepupu (anak dari amang tua) dan orang lain yang semarga dan setingkatan dengan abang-abang kita.",
                "panggilan kita kepada istrinya abang kita (angkang boru) atau biasa dipanggil angkang saja (dibaca : akkang),",
                "panggilan kita kepada pariban kita yang lebih tua dari kita (bisa juga dipanggil kakak),",
                "panggilan kita (perempuan) kepada nenek dari suami kita (angkang mangulahi).",
            ),
        ),
        Partuturan(
            title = "Anggi",
            descriptions = listOf(
                "panggilan kita kepada adik kandung kita maupun adik sepupu (anak dari amang uda) dan orang lain yang semarga dan setingkatan dengan adik-adik kita,",
                "panggilan kita kepada istrinya adik kita (anggi boru) atau biasa dipanggil inang,",
                "panggilan kita kepada pariban kita yang lebih muda dari kita (bisa juga dipanggil adik),",
                "panggilan kita (sudah tua / nenek-nenek) kepada istri dari cucu kita (anggi mangulahi).",
            ),
        ),
        Partuturan(
            title = "Tulang",
            descriptions = listOf(
                "panggilan kita kepada saudara laki-laki dari ibu kita",
                "panggilan kita kepada laki-laki yang semarga dengan ibu kita yang urutan keturunannya setingkat dengan ibu kita,",
                "panggilan kita kepada anak laki-laki dari saudara laki-laki nenek kita,",
                "panggilan kita kepada paman dari istri kita (tulang mangihut),",
                "panggilan kita kepada seorang laki-laki yang merupakan ipar dari saudara laki-laki ayah maupun ibu kita,",
                "panggilan kita (laki-laki) kepada cucu laki-laki dari paman kita / anak dari tunggane kita (tulang naposo).",
            ),
        ),
        Partuturan(
            title = "Nantulang",
            descriptions = listOf(
                "panggilan kita terhadap istri dari tulang / paman kita.",
                "panggilan kita terhadap orang yang lebih tua (perempuan) yang semarga dengan bibi kita.",
                "panggilan kita (laki-laki) kepada istri dari anaknya tunggane kita (istri dari tulang na poso kita) atau istri dari cucunya tulang kita (nantulang na poso).",
            ),
        ),
        Partuturan(
            title = "Bere",
            descriptions = listOf(
                "panggilan kita (laki-laki) kepada keponakan kita dari saudari / kepada anak dari ito kita,",
                "panggilan kita (perempuan) kepada anak dari saudari suami kita,",
                "panggilan kita kepada abang dan adik dari menantu kita yang laki-laki (abang dan adik dari hela kita).",
            ),
        ),
        Partuturan(
            title = "Maen/Parumaen",
            descriptions = listOf(
                "panggilan kita (laki-laki) kepada anak perempuan dari tunggane kita,",
                "panggilan kita (perempuan) kepada anak perempuan dari ito kita,",
                "panggilan kita kepada menantu perempuan kita (istri dari anak kita).",
            ),
        ),
        Partuturan(
            title = "Hela",
            descriptions = listOf(
                "panggilan kita kepada menantu laki-laki kita (suami dari boru kita)",
                "pada saat memanggil hela kita biasanya disebutkan dengan sebutan \"amang\" / \"amang hela\",",
                "seorang hela (menantu laki-laki) ketika dia masih berpacaran dengan boru kita (belum resmi menjadi suami dari boru kita), maka dia kita sebut dengan \"bere\" kita.",
            ),
        ),
        Partuturan(
            title = "Boru",
            descriptions = listOf(
                "Panggilan ayah kepada putrinya, terkhusus kepada yang belum menikah. Panggilan kepada setiap istri bere.",
            ),
        ),
        Partuturan(
            title = "Simatua",
            descriptions = listOf(
                "panggilan kita kepada mertua kita",
                "untuk mertua laki-laki maka disebut dengan \"amang\" / simatua doli / simatua baoa",
                "untuk mertua perempuan maka disebut \"inang\" / simatua boru",
            ),
        ),
        Partuturan(
            title = "Lae (Hanya untuk panggilan sesama laki-laki)",
            descriptions = listOf(
                "panggilan kita (laki-laki) kepada anak laki-laki dari namboru kita,",
                "panggilan kita (laki-laki) kepada suami dari saudari kita yang perempuan,",
                "panggilan \"lae\" pada sebagian besar wilayah di toba juga digunakan untuk memanggil \"tunggane\" kita.",
            ),
        ),
        Partuturan(
            title = "Tunggane (Hanya untuk panggilan sesama laki-laki)",
            descriptions = listOf(
                "panggilan kita (laki-laki) kepada saudara laki-laki dari istri kita,",
                "panggilan kita (laki-laki) kepada anak laki-laki dari tulang kita.",
            ),
        ),
        Partuturan(
            title = "Eda (Hanya untuk panggilan sesama perempuan)",
            descriptions = listOf(
                "panggilan kita (perempuan) kepada anak perempuan dari tulang kita,",
                "panggilan kita (perempuan) kepada istri dari saudara kita yang laki-laki.",
                "Panggilan sesama perempuan yang sebaya tetapi beda marga / boru (marga adalah sebutan untuk laki-laki, boru adalah sebutan untuk perempuan).",
            ),
        ),
        Partuturan(
            title = "Amangboru",
            descriptions = listOf(
                "panggilan kita terhadap suami dari saudari ayah kita,",
                "panggilan terhadap suami dari perempuan yang merupakan keturunan semarga kita yang urutannya setingkat dengan ayah kita.",
                "panggilan kita kepada suami dari namboru kita",
            ),
        ),
        Partuturan(
            title = "Namboru",
            descriptions = listOf(
                "Belakangan terkadang disingkat Bou",
                "panggilan kita terhadap saudara perempuan ayah kita,",
                "panggilan terhadap perempuan yang merupakan keturunan semarga kita yang urutannya setingkat dengan ayah kita.",
                "panggilan kita kepada istri dari amangboru kita",
            ),
        ),
        Partuturan(
            title = "Amang (Na)poso/Bapa (Na)poso/Apa (Na)poso",
            descriptions = listOf(
                "panggilan kita (perempuan) kepada keponakan laki-laki dari saudara kita yang laki-laki / anak dari ito kita,",
                "panggilan kita (perempuan) kepada laki-laki yang memangil kita \"namboru\",",
                "panggilan kita (perempuan) kepada semua laki-laki yang marganya sama dengan kita tetapi urutan keturunannya ada di bawah kita (setingkat dengan anak kita),",
                "panggilan ini di beberapa daerah disebut juga dengan istilah Paraman.",
            ),
        ),
        Partuturan(
            title = "Inang (Na)poso",
            descriptions = listOf(
                "panggilan kita (perempuan) kepada istri dari amang na poso kita",
                "panggilan kita (perempuan) kepada menantu perempuan dari saudara kita yang laki-laki",
            ),
        ),
        Partuturan(
            title = "Ito, Iboto (Hanya untuk panggilan antar lawan jenis)",
            descriptions = listOf(
                "panggilan kita sebagai laki-laki kepada saudari kita (perempuan),",
                "panggilan kita sebagai perempuan kepada saudara kita yang laki-laki,",
                "panggilan umum bagi orang kepada lawan jenisnya dalam budaya batak toba,",
                "panggilan kita (laki-laki) kepada perempuan yang semarga dan seumuran dengan kita,",
                "panggilan kita (perempuan) kepada laki-laki yang semarga dan seumuran dengan kita,",
                "panggilan kita (laki-laki) kepada saudara perempuan / ito dari ompung / bapaknya bapak kita (ito mangulahi - ito mengulangi ke atas),",
                "panggilan kita (sudah tua / nenek-nenek) kepada cucu dari saudara laki-laki / ito kita (ito mangulahi - ito mengulangi ke bawah).",
            ),
        ),
        Partuturan(
            title = "Pariban",
            descriptions = listOf(
                "panggilan kita sebagai laki-laki terhadap anak perempuan dari tulang kita,",
                "panggilan kita sebagai perempuan terhadap anak laki-laki dari namboru kita,",
                "Panggilan kita kepada laki-laki yang istrinya sama marga / boru dengan marga istri kita.",
            ),
        ),
        Partuturan(
            title = "Inang (Ibu)",
            descriptions = listOf(
                "panggilan kita terhadap perempuan yang lebih tua dari kita atau kepada orang (perempuan) yang dituakan,",
                "panggilan umum untuk menghormati semua perempuan,",
                "panggilan umum kepada menantu perempuan (inang-parumaen)",
            ),
        ),
        Partuturan(
            title = "Amang (Bapak)",
            descriptions = listOf(
                "panggilan kita terhadap pria yang lebih tua dari kita atau orang (pria) yang dituakan,",
                "panggilan umum untuk menghormati para pria,",
                "panggilan umum kepada menantu laki-laki (amang-hela).",
            ),
        ),
        Partuturan(
            title = "Inangbao",
            descriptions = listOf(
                "panggilan kita (laki-laki) kepada istri dari saudara laki-laki istri kita,",
                "panggilan kita (laki-laki) kepada istri dari tunggane kita,",
                "panggilan kita (laki-laki) kepada istri dari ipar kita.",
            ),
        ),
        Partuturan(
            title = "Amangbao",
            descriptions = listOf(
                "panggilan kita (perempuan) kepada suami dari saudara perempuan suami kita,",
                "panggilan kita (perempuan) kepada suami dari eda kita,",
                "panggilan kita (perempuan) kepada suami dari ipar kita.",
            ),
        ),
        Partuturan(
            title = "Ompung",
            descriptions = listOf(
                "Secara khusus, panggilan kepada kedua orang tua ayah dan ibu. Secara umum, panggilan kepada setiap orangtua yang usianya setara dengan orangtua ayah/ibu.",
                "Ompung secara gender ada dua, ompung doli dan ompung boru. Doli mengacu pada gender laki-laki dan boru mengacu pada gender perempuan.",
            ),
        ),
        Partuturan(
            title = "Ompung Doli (Kakek)",
            descriptions = listOf(
                "dibaca Oppung Doli",
                "panggilan khusus kepada kakek kita, ayah dari ayah/ibu kita",
            ),
        ),
        Partuturan(
            title = "Ompung Boru (Nenek)",
            descriptions = listOf(
                "dibaca Oppung Boru",
                "panggilan khusus kepada nenek kita, ibu dari ayah/ibu kita",
            ),
        ),
        Partuturan(
            title = "Pahompu",
            descriptions = listOf(
                "Pada praktiknya tidak ada yang memberi panggilan pahompu, biasanya cukup dengan nama orang tersebut.",
                "Pahompu merupakan setiap keturunan dari anak laki-laki dan perempuan.",
            ),
        ),
    )

    private val umpasas = (1..100).map {
        Umpasa(
            category = if (Random.nextBoolean()) UmpasaCategory.FUNERAL else UmpasaCategory.WEDDING,
            content = "$it content lorem ipsum dolor sit amet",
            meaning = "$it meaning lorem ipsum dolor",
        )
    }

    override suspend fun getPartuturans(): List<Partuturan> {
        return partuturans.sortedBy { it.title }
    }

    override suspend fun getUmpasas(
        category: UmpasaCategory,
    ): List<Umpasa> {
        return if (category == UmpasaCategory.ALL) umpasas
        else umpasas.filter { it.category === category }
    }
}