package com.example.data.local

import com.example.data.model.ContentPack
import com.example.data.model.Exercise
import com.example.data.model.ExerciseType
import com.example.data.model.LearningUnit
import com.example.data.model.Lesson
import com.example.data.model.ReadingText
import com.example.data.model.WordItem

data class AlphabetLetter(
    val arabic: String,
    val name: String,
    val transliteration: String,
    val isolated: String,
    val initial: String,
    val medial: String,
    val final: String,
    val exampleWord: String,
    val exampleMeaning: String
)

object InitialCurriculum {

    val ALPHABET_LETTERS = listOf(
        AlphabetLetter("ا", "Alif", "A / O", "ا", "ا", "ـا", "ـا", "أَسَدٌ", "Sher"),
        AlphabetLetter("ب", "Ba", "B", "ب", "بـ", "ـبـ", "ـب", "بَيْتٌ", "Uy"),
        AlphabetLetter("ت", "Ta", "T", "ت", "تـ", "ـتـ", "ـت", "تُفَّاحٌ", "Olma"),
        AlphabetLetter("ث", "Sa (tilsim)", "S", "ث", "ثـ", "ـثـ", "ـث", "ثَوْبٌ", "Kiyim"),
        AlphabetLetter("ج", "Jim", "J", "ج", "جـ", "ـجـ", "ـج", "جَمَلٌ", "Tuya"),
        AlphabetLetter("ح", "Ha (tomoq)", "H", "ح", "حـ", "ـحـ", "ـح", "حَلِيبٌ", "Sut"),
        AlphabetLetter("خ", "Xo (xirqiroq)", "X", "خ", "خـ", "ـخـ", "ـخ", "خُبْزٌ", "Non"),
        AlphabetLetter("د", "Dal", "D", "د", "د", "ـد", "ـد", "دَرْسٌ", "Dars"),
        AlphabetLetter("ذ", "Zal (tilsim)", "Z", "ذ", "ذ", "ـذ", "ـذ", "ذَهَبٌ", "Oltin"),
        AlphabetLetter("ر", "Ro", "R", "ر", "ر", "ـر", "ـر", "رَأْسٌ", "Bosh"),
        AlphabetLetter("ز", "Zay", "Z", "ز", "ز", "ـز", "ـز", "زَهْرَةٌ", "Gul"),
        AlphabetLetter("س", "Sin", "S", "س", "سـ", "ـسـ", "ـس", "سَمَكٌ", "Baliq"),
        AlphabetLetter("ش", "Shin", "Sh", "ش", "شـ", "ـشـ", "ـش", "شَمْسٌ", "Quyosh"),
        AlphabetLetter("ص", "Sod (yo'g'on)", "S", "ص", "صـ", "ـصـ", "ـص", "صَبَاحٌ", "Ertalab"),
        AlphabetLetter("ض", "Zod (yo'g'on)", "Z", "ض", "ضـ", "ـضـ", "ـض", "ضَوْءٌ", "Nur / Yorug'lik"),
        AlphabetLetter("ط", "To (yo'g'on)", "T", "ط", "طـ", "ـطـ", "ـط", "طَالِبٌ", "Talaba"),
        AlphabetLetter("ظ", "Zo (yo'g'on tilsim)", "Z", "ظ", "ظـ", "ـظـ", "ـظ", "ظِلٌّ", "Soya"),
        AlphabetLetter("ع", "Ayn (tomoq)", "'A", "ع", "عـ", "ـعـ", "ـع", "عَيْنٌ", "Ko'z / Buloq"),
        AlphabetLetter("غ", "G'oyn", "G'", "غ", "غـ", "ـغـ", "ـغ", "غَابَةٌ", "O'rmon"),
        AlphabetLetter("ف", "Fa", "F", "ف", "فـ", "ـفـ", "ـف", "فَمٌ", "Og'iz"),
        AlphabetLetter("ق", "Qof (chuqur)", "Q", "ق", "قـ", "ـقـ", "ـق", "قَلَمٌ", "Qalam"),
        AlphabetLetter("ك", "Kaf", "K", "ك", "كـ", "ـكـ", "ـك", "كِتَابٌ", "Kitob"),
        AlphabetLetter("ل", "Lam", "L", "ل", "لـ", "ـلـ", "ـل", "لَحْمٌ", "Go'sht"),
        AlphabetLetter("م", "Mim", "M", "م", "مـ", "ـمـ", "ـم", "مَاءٌ", "Suv"),
        AlphabetLetter("ن", "Nun", "N", "ن", "نـ", "ـنـ", "ـن", "نَارٌ", "Olov"),
        AlphabetLetter("ه", "Ha (yumshoq)", "H", "ه", "هـ", "ـهـ", "ـه", "هِلَالٌ", "Yangi oy"),
        AlphabetLetter("و", "Vov", "V / U", "و", "و", "ـو", "ـو", "وَلَدٌ", "Bola"),
        AlphabetLetter("ي", "Yo", "Y / I", "ي", "يـ", "ـيـ", "ـي", "يَدٌ", "Qo'l")
    )

    val INITIAL_PACKS = listOf(
        ContentPack(
            id = "pack_1",
            title = "1-to'plam: Alifbo va Kundalik So'zlar",
            description = "Arab alfaviti, harakatlar, salomlashish, oila va eng muhim boshlang'ich so'zlar",
            version = 1,
            isPublished = true,
            wordCount = 105
        )
    )

    // 100+ Vocabulary words with full harakat, transliteration, Uzbek meanings, and example sentences
    val INITIAL_WORDS: List<WordItem> = listOf(
        // Salomlashish va odob
        WordItem("w_1", "السَّلَامُ عَلَيْكُمْ", "Assalomu alaykum", "As-salāmu 'alaykum", "Salomlashish", "Boshlang'ich", "السَّلَامُ عَلَيْكُمْ يَا صَدِيقِي", "Assalomu alaykum, ey do'stim!"),
        WordItem("w_2", "وَعَلَيْكُمُ السَّلَامُ", "Vaalaykum assalom", "Wa 'alaykumu s-salām", "Salomlashish", "Boshlang'ich", "وَعَلَيْكُمُ السَّلَامُ وَرَحْمَةُ اللهِ", "Vaalaykum assalom va rahmatulloh"),
        WordItem("w_3", "مَرْحَبًا", "Salom", "Marhaban", "Salomlashish", "Boshlang'ich", "مَرْحَبًا بِكُمْ جَمِيعًا", "Barchangizga salom!"),
        WordItem("w_4", "أَهْلًا وَسَهْلًا", "Xush kelibsiz", "Ahlan wa sahlan", "Salomlashish", "Boshlang'ich", "أَهْلًا وَسَهْلًا فِي بَيْتِنَا", "Uyimizga xush kelibsiz!"),
        WordItem("w_5", "شُكْرًا", "Rahmat / Tashakkur", "Shukran", "Salomlashish", "Boshlang'ich", "شُكْرًا جَزِيلًا لَكَ", "Sizga katta rahmat!"),
        WordItem("w_6", "عَفْوًا", "Arzimaydi / Kechirasiz", "'Afwan", "Salomlashish", "Boshlang'ich", "عَفْوًا، لَا شُكْرَ عَلَى وَاجِبٍ", "Arzimaydi, minnatdorchilikka hojat yo'q"),
        WordItem("w_7", "مَعَ السَّلَامَةِ", "Xayr / Salomat bo'ling", "Ma'a s-salāmah", "Salomlashish", "Boshlang'ich", "مَعَ السَّلَامَةِ، إِلَى اللِّقَاءِ", "Xayr, ko'rishguncha!"),
        WordItem("w_8", "صَبَاحُ الخَيْرِ", "Xayrli tong", "Sabāhu l-khayr", "Salomlashish", "Boshlang'ich", "صَبَاحُ الخَيْرِ يَا أُمِّي", "Xayrli tong, onajon!"),
        WordItem("w_9", "مَسَاءُ الخَيْرِ", "Xayrli kech", "Masā'u l-khayr", "Salomlashish", "Boshlang'ich", "مَسَاءُ الخَيْرِ يَا أَبِي", "Xayrli kech, otajon!"),
        WordItem("w_10", "كَيْفَ حَالُكَ؟", "Qalaysiz? (erkakka)", "Kayfa hāluk?", "Salomlashish", "Boshlang'ich", "كَيْفَ حَالُكَ اليَوْمَ؟", "Bugun ahvolingiz qanday?"),
        WordItem("w_11", "أَنَا بِخَيْرٍ", "Men yaxshiman", "Anā bi-khayr", "Salomlashish", "Boshlang'ich", "أَنَا بِخَيْرٍ، الحَمْدُ للهِ", "Men yaxshiman, Allohga shukr"),
        WordItem("w_12", "نَعَمْ", "Ha", "Na'am", "Salomlashish", "Boshlang'ich", "نَعَمْ، أَنَا طَالِبٌ", "Ha, men talabaman"),
        WordItem("w_13", "لَا", "Yo'q", "Lā", "Salomlashish", "Boshlang'ich", "لَا، لَسْتُ تَعِبًا", "Yo'q, men charchamadim"),
        WordItem("w_14", "مِنْ فَضْلِكَ", "Iltimos", "Min fadlik", "Salomlashish", "Boshlang'ich", "كُوبَ مَاءٍ مِنْ فَضْلِكَ", "Bir stakan suv, iltimos"),

        // Oila va odamlar
        WordItem("w_15", "أَبٌ", "Ota", "Abun", "Oila", "Boshlang'ich", "أَبِي يَعْمَلُ فِي المَدْرَسَةِ", "Otam maktabda ishlaydi"),
        WordItem("w_16", "أُمٌّ", "Ona", "Ummun", "Oila", "Boshlang'ich", "أُمِّي تُحِبُّ القِرَاءَةَ", "Onam kitob o'qishni yaxshi ko'radi"),
        WordItem("w_17", "أَخٌ", "Aka / Uka", "Akhun", "Oila", "Boshlang'ich", "أَخِي الكَبِيرُ طَبِيبٌ", "Katta akam shifokor"),
        WordItem("w_18", "أُخْتٌ", "Opa / Singil", "Ukhtun", "Oila", "Boshlang'ich", "أُخْتِي الصَّغِيرَةُ تَدْرُسُ", "Kichik singlim o'qiydi"),
        WordItem("w_19", "اِبْنٌ", "O'g'il farzand", "Ibnun", "Oila", "Boshlang'ich", "هَذَا اِبْنِي", "Bu mening o'g'lim"),
        WordItem("w_20", "بِنْتٌ", "Qiz farzand", "Bintun", "Oila", "Boshlang'ich", "البِنْتُ تَلْعَبُ فِي الحَدِيقَةِ", "Qiz bog'da o'ynamoqda"),
        WordItem("w_21", "جَدٌّ", "Bobo", "Jaddun", "Oila", "Boshlang'ich", "جَدِّي رَجُلٌ حَكِيمٌ", "Bobom dono inson"),
        WordItem("w_22", "جَدَّةٌ", "Buvi", "Jaddatun", "Oila", "Boshlang'ich", "جَدَّتِي تَحْكِي قِصَصًا", "Buvim ertaklar aytib beradi"),
        WordItem("w_23", "أُسْرَةٌ", "Oila", "Usratun", "Oila", "Boshlang'ich", "أُسْرَتِي سَعِيدَةٌ", "Mening oilam baxtli"),
        WordItem("w_24", "صَدِيقٌ", "Do'st (o'g'il)", "Sadīqun", "Oila", "Boshlang'ich", "زَيْدٌ صَدِيقِي المُخْلِصُ", "Zayd mening sodiq do'stim"),
        WordItem("w_25", "صَدِيقَةٌ", "Dugona (qiz)", "Sadīqatun", "Oila", "Boshlang'ich", "مَرْيَمُ صَدِيقَتِي", "Maryam mening dugonam"),
        WordItem("w_26", "رَجُلٌ", "Erkak kishi", "Rajulun", "Oila", "Boshlang'ich", "هَذَا رَجُلٌ طَيِّبٌ", "Bu yaxshi erkak kishi"),
        WordItem("w_27", "اِمْرَأَةٌ", "Ayol kishi", "Imra'atun", "Oila", "Boshlang'ich", "المَرْأَةُ تُعَلِّمُ الأَطْفَالَ", "Ayol bolalarga ta'lim beryapti"),
        WordItem("w_28", "وَلَدٌ", "O'g'il bola", "Waladun", "Oila", "Boshlang'ich", "الوَلَدُ يَكْتُبُ الدَّرْسَ", "Bola darsni yozyapti"),

        // Sonlar
        WordItem("w_29", "وَاحِدٌ", "Bir (1)", "Wāhidun", "Sonlar", "Boshlang'ich", "كِتَابٌ وَاحِدٌ", "Bitta kitob"),
        WordItem("w_30", "اِثْنَانِ", "Ikki (2)", "Ithnāni", "Sonlar", "Boshlang'ich", "قَلَمَانِ اِثْنَانِ", "Ikkita qalam"),
        WordItem("w_31", "ثَلَاثَةٌ", "Uch (3)", "Thalāthatun", "Sonlar", "Boshlang'ich", "ثَلَاثَةُ أَيَّامٍ", "Uch kun"),
        WordItem("w_32", "أَرْبَعَةٌ", "To'rt (4)", "Arba'atun", "Sonlar", "Boshlang'ich", "أَرْبَعَةُ إِخْوَةٍ", "To'rtta aka-uka"),
        WordItem("w_33", "خَمْسَةٌ", "Besh (5)", "Khamsatun", "Sonlar", "Boshlang'ich", "خَمْسُ صَلَوَاتٍ فِي اليَوْمِ", "Kunda besh vaqt namoz"),
        WordItem("w_34", "سِتَّةٌ", "Olti (6)", "Sittatun", "Sonlar", "Boshlang'ich", "سِتَّةُ أَشْهُرٍ", "Olti oy"),
        WordItem("w_35", "سَبْعَةٌ", "Yetti (7)", "Sab'atun", "Sonlar", "Boshlang'ich", "سَبْعُ سَمَاوَاتٍ", "Yetti qat osmon"),
        WordItem("w_36", "ثَمَانِيَةٌ", "Sakkiz (8)", "Thamāniyatun", "Sonlar", "Boshlang'ich", "ثَمَانِي سَاعَاتٍ", "Sakkiz soat"),
        WordItem("w_37", "تِسْعَةٌ", "To'qqiz (9)", "Tis'atun", "Sonlar", "Boshlang'ich", "تِسْعَةُ دَرَاهِمَ", "To'qqiz dirham"),
        WordItem("w_38", "عَشَرَةٌ", "O'n (10)", "‘Asharatun", "Sonlar", "Boshlang'ich", "عَشَرَةُ كُتُبٍ", "O'nta kitob"),
        WordItem("w_39", "صِفْرٌ", "Nol (0)", "Sifrun", "Sonlar", "Boshlang'ich", "دَرَجَةُ الحَرَارَةِ صِفْرٌ", "Harorat nol daraja"),

        // Taom va ichimliklar
        WordItem("w_40", "خُبْزٌ", "Non", "Khubzun", "Taom", "Boshlang'ich", "آكُلُ خُبْزًا طَازَجًا", "Yangi non yeyapman"),
        WordItem("w_41", "مَاءٌ", "Suv", "Mā'un", "Taom", "Boshlang'ich", "أَشْرَبُ مَاءً بَارِدًا", "Muzdek suv ichyapman"),
        WordItem("w_42", "حَلِيبٌ", "Sut", "Halībun", "Taom", "Boshlang'ich", "الحَلِيبُ مُفِيدٌ لِلصِّحَّةِ", "Sut salomatlik uchun foydali"),
        WordItem("w_43", "شَايٌ", "Choy", "Shāyun", "Taom", "Boshlang'ich", "أُفَضِّلُ الشَّايَ الأَخْضَرَ", "Ko'k choyni afzal ko'raman"),
        WordItem("w_44", "قَهْوَةٌ", "Qahva", "Qahwatun", "Taom", "Boshlang'ich", "رَائِحَةُ القَهْوَةِ زَكِيَّةٌ", "Qahvaning hidi xushbo'y"),
        WordItem("w_45", "لَحْمٌ", "Go'sht", "Lahmun", "Taom", "Boshlang'ich", "لَحْمُ الخَرُوفِ لَذِيذٌ", "Qo'y go'shti mazali"),
        WordItem("w_46", "سَمَكٌ", "Baliq", "Samakun", "Taom", "Boshlang'ich", "السَّمَكُ طَعَامٌ صِحِّيٌّ", "Baliq foydali taom"),
        WordItem("w_47", "تُفَّاحٌ", "Olma", "Tuffāhun", "Taom", "Boshlang'ich", "تُفَّاحَةٌ حَمْرَاءُ حُلْوَةٌ", "Shirin qizil olma"),
        WordItem("w_48", "عَسَلٌ", "Asal", "‘Asalun", "Taom", "Boshlang'ich", "العَسَلُ فِيهِ شِفَاءٌ", "Asalda shifo bor"),
        WordItem("w_49", "تَمْرٌ", "Xurmo", "Tamrun", "Taom", "Boshlang'ich", "التَّمْرُ غِذَاءٌ مُبَارَكٌ", "Xurmo muborak ozuqadir"),
        WordItem("w_50", "مِلْحٌ", "Tuz", "Milhun", "Taom", "Boshlang'ich", "الطَّعَامُ يَحْتَاجُ إِلَى مِلْحٍ", "Taomga tuz kerak"),
        WordItem("w_51", "سُكَّرٌ", "Shakar", "Sukkarun", "Taom", "Boshlang'ich", "شَايٌ بِلَا سُكَّرٍ", "Shakarsiz choy"),
        WordItem("w_52", "زَيْتٌ", "Yog'", "Zaytun", "Taom", "Boshlang'ich", "زَيْتُ الزَّيْتُونِ طَيِّبٌ", "Zaytun moyi juda yaxshi"),

        // Ranglar
        WordItem("w_53", "أَبْيَضُ", "Oq (erkak jinsida)", "Abyadu", "Ranglar", "Boshlang'ich", "قَمِيصٌ أَبْيَضُ نَظِيفٌ", "Toza oq ko'ylak"),
        WordItem("w_54", "أَسْوَدُ", "Qora", "Aswadu", "Ranglar", "Boshlang'ich", "حِصَانٌ أَسْوَدُ سَرِيعٌ", "Tez yurar qora ot"),
        WordItem("w_55", "أَحْمَرُ", "Qizil", "Ahmaru", "Ranglar", "Boshlang'ich", "وَرْدَةٌ حَمْرَاءُ جَمِيلَةٌ", "Chiroyli qizil gul"),
        WordItem("w_56", "أَزْرَقُ", "Ko'k / Havorang", "Azraqu", "Ranglar", "Boshlang'ich", "السَّمَاءُ زَرْقَاءُ صَافِيَةٌ", "Osmon musaffo moviy"),
        WordItem("w_57", "أَخْضَرُ", "Yashil", "Akhdaru", "Ranglar", "Boshlang'ich", "الشَّجَرُ أَخْضَرُ مُورِقٌ", "Daraxt yaproqli yashil"),
        WordItem("w_58", "أَصْفَرُ", "Sariq", "Asfaru", "Ranglar", "Boshlang'ich", "الشَّمْسُ ذَهَبِيَّةٌ صَفْرَاءُ", "Quyosh oltinrang sariq"),

        // Tana a'zolari
        WordItem("w_59", "رَأْسٌ", "Bosh", "Ra'sun", "Tana", "Boshlang'ich", "أَشْعُرُ بِأَلَمٍ فِي رَأْسِي", "Boshim og'riyapti"),
        WordItem("w_60", "عَيْنٌ", "Ko'z", "‘Aynun", "Tana", "Boshlang'ich", "العَيْنُ نِعْمَةٌ عَظِيمَةٌ", "Ko'z ulug' ne'matdir"),
        WordItem("w_61", "أَنْفٌ", "Burun", "Anfun", "Tana", "Boshlang'ich", "أَتَنَفَّسُ بِالأَنْفِ", "Burun orqali nafas olaman"),
        WordItem("w_62", "فَمٌ", "Og'iz", "Famun", "Tana", "Boshlang'ich", "اِفْتَحْ فَمَكَ عِنْدَ الطَّبِيبِ", "Shifokor huzurida og'zingni och"),
        WordItem("w_63", "قَلْبٌ", "Yurak / Qalb", "Qalbun", "Tana", "Boshlang'ich", "قَلْبٌ سَلِيمٌ وَمُطْمَئِنٌّ", "Sog'lom va xotirjam qalb"),
        WordItem("w_64", "يَدٌ", "Qo'l", "Yadun", "Tana", "Boshlang'ich", "اِغْسِلْ يَدَيْكَ بِالمَاءِ", "Qo'llaringni suv bilan yuv"),
        WordItem("w_65", "رِجْلٌ", "Oyoq", "Rijlun", "Tana", "Boshlang'ich", "أَمْشِي عَلَى رِجْلَيَّ", "Oyoqlarimda yuraman"),
        WordItem("w_66", "أُذُنٌ", "Quloq", "Udhunun", "Tana", "Boshlang'ich", "أَسْمَعُ بِالأُذُنِ", "Quloq bilan eshitaman"),
        WordItem("w_67", "لِسَانٌ", "Til", "Lisānun", "Tana", "Boshlang'ich", "اللِّسَانُ العَرَبِيُّ جَمِيلٌ", "Arab tili go'zaldir"),

        // Uy va Maktab
        WordItem("w_68", "بَيْتٌ", "Uy", "Baytun", "Joylar", "Boshlang'ich", "بَيْتِي قَرِيبٌ مِنَ المَسْجِدِ", "Uyim masjidga yaqin"),
        WordItem("w_69", "بَابٌ", "Eshik", "Bābun", "Joylar", "Boshlang'ich", "اِفْتَحِ البَابَ يَا عَلِيُّ", "Eshikni och, ey Ali!"),
        WordItem("w_70", "نَافِذَةٌ", "Deraza", "Nāfidhatun", "Joylar", "Boshlang'ich", "النَّافِذَةُ مَفْتُوحَةٌ", "Deraza ochiq"),
        WordItem("w_71", "غُرْفَةٌ", "Xona", "Ghurfatun", "Joylar", "Boshlang'ich", "غُرْفَةُ النَّوْمِ مُرَتَّبَةٌ", "Yotoqxona tartibli"),
        WordItem("w_72", "مَسْجِدٌ", "Masjid", "Masjidun", "Joylar", "Boshlang'ich", "أُصَلِّي فِي المَسْجِدِ", "Masjidda namoz o'qiyman"),
        WordItem("w_73", "مَدْرَسَةٌ", "Maktab", "Madrasatun", "Joylar", "Boshlang'ich", "المَدْرَسَةُ مَكَانُ العِلْمِ", "Maktab ilm maskanidir"),
        WordItem("w_74", "جَامِعَةٌ", "Universitet", "Jāmi‘atun", "Joylar", "Boshlang'ich", "أَدْرُسُ فِي الجَامِعَةِ", "Universitetda o'qiyman"),
        WordItem("w_75", "سُوقٌ", "Bozor", "Sūqun", "Joylar", "Boshlang'ich", "أَذْهَبُ إِلَى السُّوقِ", "Bozorga ketyapman"),
        WordItem("w_76", "شَارِعٌ", "Ko'cha", "Shāri‘un", "Joylar", "Boshlang'ich", "الشَّارِعُ وَاسِعٌ وَنَظِيفٌ", "Ko'cha keng va toza"),
        WordItem("w_77", "مَدِينَةٌ", "Shahar", "Madīnatun", "Joylar", "Boshlang'ich", "طَشْقَنْدُ مَدِينَةٌ كَبِيرَةٌ", "Toshkent katta shahar"),

        // O'quv anjomlari
        WordItem("w_78", "كِتَابٌ", "Kitob", "Kitābun", "Ta'lim", "Boshlang'ich", "هَذَا كِتَابٌ مُفِيدٌ", "Bu foydali kitob"),
        WordItem("w_79", "قَلَمٌ", "Qalam", "Qalamun", "Ta'lim", "Boshlang'ich", "القَلَمُ يَكْتُبُ بِوُضُوحٍ", "Qalam aniq yozyapti"),
        WordItem("w_80", "دَفْتَرٌ", "Daftar", "Daftarun", "Ta'lim", "Boshlang'ich", "أَكْتُبُ فِي الدَّفْتَرِ", "Daftarga yozyapman"),
        WordItem("w_81", "طَالِبٌ", "Talaba / O'quvchi", "Tālibun", "Ta'lim", "Boshlang'ich", "الطَّالِبُ يَجْتَهِدُ فِي دَرْسِهِ", "Talaba darsida tirishqoq"),
        WordItem("w_82", "مُعَلِّمٌ", "O'qituvchi / Ustoz", "Mu‘allimun", "Ta'lim", "Boshlang'ich", "المُعَلِّمُ يَشْرَحُ الدَّرْسَ", "O'qituvchi darsni tushuntiryapti"),
        WordItem("w_83", "مَكْتَبٌ", "Yozuv stoli / Parta", "Maktabun", "Ta'lim", "Boshlang'ich", "الكِتَابُ عَلَى المَكْتَبِ", "Kitob stol ustida"),
        WordItem("w_84", "كُرْسِيٌّ", "Stul", "Kursiyyun", "Ta'lim", "Boshlang'ich", "أَجْلِسُ عَلَى الكُرْسِيِّ", "Stulda o'tiribman"),

        // Vaqt
        WordItem("w_85", "يَوْمٌ", "Kun", "Yawmun", "Vaqt", "Boshlang'ich", "يَوْمٌ جَمِيلٌ وَمُشْرِقٌ", "Chiroyli va yorug' kun"),
        WordItem("w_86", "لَيْلٌ", "Tun / Kecha", "Laylun", "Vaqt", "Boshlang'ich", "اللَّيْلُ لِلرَّاحَةِ", "Tun dam olish uchundir"),
        WordItem("w_87", "نَهَارٌ", "Kunduz", "Nahārun", "Vaqt", "Boshlang'ich", "النَّهَارُ لِلعَمَلِ", "Kunduz ishlash uchundir"),
        WordItem("w_88", "اليَوْمَ", "Bugun", "Al-yawma", "Vaqt", "Boshlang'ich", "اليَوْمَ سَأَبْدَأُ الدَّرْسَ", "Bugun darsni boshlayman"),
        WordItem("w_89", "غَدًا", "Ertaga", "Ghadan", "Vaqt", "Boshlang'ich", "أَرَاكَ غَدًا إِنْ شَاءَ اللهُ", "Ertaga ko'rishamiz inshaalloh"),
        WordItem("w_90", "أَمْسِ", "Kecha (o'tgan kun)", "Amsi", "Vaqt", "Boshlang'ich", "قَرَأْتُ الكِتَابَ أَمْسِ", "Kitobni kecha o'qidim"),
        WordItem("w_91", "سَاعَةٌ", "Soat", "Sā‘atun", "Vaqt", "Boshlang'ich", "كَمِ السَّاعَةُ الآنَ؟", "Hozir soat necha?"),

        // Muhim fe'llar
        WordItem("w_92", "قَرَأَ", "O'qidi", "Qara'a", "Fe'llar", "Boshlang'ich", "قَرَأَ الوَلَدُ القُرْآنَ", "Bola Qur'on o'qidi"),
        WordItem("w_93", "كَتَبَ", "Yozdi", "Kataba", "Fe'llar", "Boshlang'ich", "كَتَبَ الطَّالِبُ رِسَالَةً", "Talaba xat yozdi"),
        WordItem("w_94", "ذَهَبَ", "Ketdi / Bordi", "Dhahaba", "Fe'llar", "Boshlang'ich", "ذَهَبَ أَحْمَدُ إِلَى المَسْجِدِ", "Ahmad masjidga ketdi"),
        WordItem("w_95", "جَاءَ", "Keldi", "Jā'a", "Fe'llar", "Boshlang'ich", "جَاءَ أَبِي مِنَ السَّفَرِ", "Otam safardan keldi"),
        WordItem("w_96", "أَكَلَ", "Yedi", "Akala", "Fe'llar", "Boshlang'ich", "أَكَلَ التُّفَّاحَةَ اللَذِيذَةَ", "Mazali olmani yedi"),
        WordItem("w_97", "شَرِبَ", "Ichdi", "Shariba", "Fe'llar", "Boshlang'ich", "شَرِبَ مَاءً زُلَالًا", "Chuchuk suv ichdi"),
        WordItem("w_98", "جَلَسَ", "O'tirdi", "Jalasa", "Fe'llar", "Boshlang'ich", "جَلَسَ عَلَى الكُرْسِيِّ", "Stulga o'tirdi"),
        WordItem("w_99", "قَامَ", "O'rnidan turdi", "Qāma", "Fe'llar", "Boshlang'ich", "قَامَ فِي الصَّبَاحِ بَاكِرًا", "Ertalab barvaqt turdi"),
        WordItem("w_100", "سَمِعَ", "Eshitdi", "Sami‘a", "Fe'llar", "Boshlang'ich", "سَمِعَ الأَذَانَ", "Azonni eshitdi"),
        WordItem("w_101", "رَأَى", "Ko'rdi", "Ra'ā", "Fe'llar", "Boshlang'ich", "رَأَى هِلَالَ رَمَضَانَ", "Ramazon hilolini ko'rdi"),
        WordItem("w_102", "تَعَلَّمَ", "O'rgandi", "Ta‘allama", "Fe'llar", "Boshlang'ich", "تَعَلَّمَ العَرَبِيَّةَ بِسُهُولَةٍ", "Arab tilini oson o'rgandi"),

        // Ko'rsatish olmoshlari va bog'lovchilar
        WordItem("w_103", "هَذَا", "Bu (muzakkar / erkak jinsiga)", "Hādhā", "Jumlalar", "Boshlang'ich", "هَذَا كِتَابٌ جَمِيلٌ", "Bu chiroyli kitob"),
        WordItem("w_104", "هَذِهِ", "Bu (muannas / ayol jinsiga)", "Hādhihi", "Jumlalar", "Boshlang'ich", "هَذِهِ مَدْرَسَتِي", "Bu mening maktabim"),
        WordItem("w_105", "هُوَ", "U (erkakka nisbatan)", "Huwa", "Jumlalar", "Boshlang'ich", "هُوَ طَالِبٌ مُجْتَهِدٌ", "U tirishqoq talabadir"),
        WordItem("w_106", "هِيَ", "U (ayolga nisbatan)", "Hiya", "Jumlalar", "Boshlang'ich", "هِيَ مُعَلِّمَةٌ فَاضِلَةٌ", "U fazilatli muallimadir"),
        WordItem("w_107", "أَنَا", "Men", "Anā", "Jumlalar", "Boshlang'ich", "أَنَا أُحِبُّ اللُّغَةَ العَرَبِيَّةَ", "Men arab tilini yaxshi ko'raman")
    )

    val UNITS: List<LearningUnit> = listOf(
        LearningUnit(
            id = "unit_1",
            orderIndex = 1,
            title = "1-Bo'lim: Arab Alfaviti va Harflar",
            description = "Arab yozuvini o'qish, harflarning bosh, o'rta va oxirgi shakllarini tanib olish",
            level = "Boshlang'ich",
            colorHex = "#58CC02", // Duolingo Green
            lessons = listOf(
                Lesson(
                    id = "u1_l1",
                    unitId = "unit_1",
                    title = "Alif, Ba, Ta, Sa",
                    description = "Birinchi 4 ta harf bilan tanishuv va ularni ajratish",
                    orderIndex = 1,
                    xpReward = 15,
                    exercises = listOf(
                        Exercise(
                            id = "ex_1",
                            type = ExerciseType.LETTER_RECOGNITION,
                            prompt = "Qaysi harf 'Alif' (ا)?",
                            arabicText = "ا",
                            uzbekText = "Alif harfi o'ngdan chapga chiziq shaklida yoziladi",
                            options = listOf("ا", "ب", "ت", "ث"),
                            correctAnswer = "ا",
                            explanation = "'Alif' (ا) - arab alifbosining birinchi harfi"
                        ),
                        Exercise(
                            id = "ex_2",
                            type = ExerciseType.MULTIPLE_CHOICE_AR_TO_UZ,
                            prompt = "'ب' harfining nomi nima?",
                            arabicText = "ب",
                            options = listOf("Ba", "Ta", "Sa", "Jim"),
                            correctAnswer = "Ba",
                            explanation = "Tagida bitta nuqtasi bor qayiqsimon harf - 'Ba' (ب)"
                        ),
                        Exercise(
                            id = "ex_3",
                            type = ExerciseType.MATCHING_PAIRS,
                            prompt = "Harflarni ularning nomlari bilan moslashtiring",
                            pairs = mapOf(
                                "ا" to "Alif",
                                "ب" to "Ba",
                                "ت" to "Ta",
                                "ث" to "Sa"
                            )
                        ),
                        Exercise(
                            id = "ex_4",
                            type = ExerciseType.LISTENING,
                            prompt = "Talaffuzni tinglang va to'g'ri so'zni tanlang",
                            arabicText = "بَيْتٌ",
                            uzbekText = "Baytun (Uy)",
                            options = listOf("بَيْتٌ", "كِتَابٌ", "مَاءٌ", "قَلَمٌ"),
                            correctAnswer = "بَيْتٌ",
                            explanation = "'Baytun' - arabchada 'Uy' degan ma'noni bildiradi"
                        ),
                        Exercise(
                            id = "ex_5",
                            type = ExerciseType.MULTIPLE_CHOICE_UZ_TO_AR,
                            prompt = "Ustida ikkita nuqtasi bor harf qaysi?",
                            uzbekText = "Ta harfi",
                            options = listOf("ت", "ب", "ث", "ن"),
                            correctAnswer = "ت",
                            explanation = "'Ta' (ت) ustida ikki nuqtaga ega"
                        ),
                        Exercise(
                            id = "ex_6",
                            type = ExerciseType.TYPING,
                            prompt = "'بَيْتٌ' (Bayt) so'zining o'zbekcha tarjimasini yozing",
                            arabicText = "بَيْتٌ",
                            correctAnswer = "Uy",
                            explanation = "بَيْتٌ = Uy"
                        ),
                        Exercise(
                            id = "ex_7",
                            type = ExerciseType.SENTENCE_BUILDER,
                            prompt = "So'zlarni tartibga solib: 'Bu mening uyim' tuzing",
                            uzbekText = "Bu uy",
                            sentenceWords = listOf("هَذَا", "بَيْتٌ"),
                            correctAnswer = "هَذَا بَيْتٌ",
                            explanation = "هَذَا (Bu) + بَيْتٌ (uy)"
                        )
                    )
                ),
                Lesson(
                    id = "u1_l2",
                    unitId = "unit_1",
                    title = "Jim, Ha, Xo",
                    description = "Tomoq va xirqiroq tovushlar: ج , ح , خ",
                    orderIndex = 2,
                    xpReward = 15,
                    exercises = listOf(
                        Exercise(
                            id = "ex_2_1",
                            type = ExerciseType.LETTER_RECOGNITION,
                            prompt = "Ichida nuqtasi bor harf qaysi?",
                            arabicText = "ج",
                            options = listOf("ج", "ح", "خ", "ع"),
                            correctAnswer = "ج",
                            explanation = "'Jim' (ج) harfining qornida nuqtasi bo'ladi"
                        ),
                        Exercise(
                            id = "ex_2_2",
                            type = ExerciseType.MULTIPLE_CHOICE_AR_TO_UZ,
                            prompt = "'حَلِيبٌ' (Halib) so'zi nimani anglatadi?",
                            arabicText = "حَلِيبٌ",
                            options = listOf("Sut", "Non", "Suv", "Choy"),
                            correctAnswer = "Sut",
                            explanation = "حَلِيبٌ = Sut"
                        ),
                        Exercise(
                            id = "ex_2_3",
                            type = ExerciseType.MATCHING_PAIRS,
                            prompt = "So'z va ma'nolarini juftlang",
                            pairs = mapOf(
                                "خُبْزٌ" to "Non",
                                "حَلِيبٌ" to "Sut",
                                "جَمَلٌ" to "Tuya",
                                "مَاءٌ" to "Suv"
                            )
                        ),
                        Exercise(
                            id = "ex_2_4",
                            type = ExerciseType.LISTENING,
                            prompt = "So'zni eshiting va to'g'ri variantni belgilang",
                            arabicText = "خُبْزٌ",
                            options = listOf("خُبْزٌ", "لَحْمٌ", "تَمْرٌ", "عَسَلٌ"),
                            correctAnswer = "خُبْزٌ",
                            explanation = "خُبْزٌ = Non"
                        ),
                        Exercise(
                            id = "ex_2_5",
                            type = ExerciseType.SENTENCE_BUILDER,
                            prompt = "Arabcha jumla tuzing: 'Bu yangi non'",
                            uzbekText = "Bu non",
                            sentenceWords = listOf("هَذَا", "خُبْزٌ"),
                            correctAnswer = "هَذَا خُبْزٌ",
                            explanation = "هَذَا (Bu) + خُبْزٌ (non)"
                        )
                    )
                ),
                Lesson(
                    id = "u1_l3",
                    unitId = "unit_1",
                    title = "Dal, Zal, Ro, Za",
                    description = "O'zidan keyingi harfga ulanmaydigan harflar",
                    orderIndex = 3,
                    xpReward = 15,
                    exercises = listOf(
                        Exercise(
                            id = "ex_3_1",
                            type = ExerciseType.MULTIPLE_CHOICE_AR_TO_UZ,
                            prompt = "'دَرْسٌ' (Dars) so'zining ma'nosi nima?",
                            arabicText = "دَرْسٌ",
                            options = listOf("Dars", "Kitob", "Maktab", "Qalam"),
                            correctAnswer = "Dars",
                            explanation = "دَرْسٌ = Dars"
                        ),
                        Exercise(
                            id = "ex_3_2",
                            type = ExerciseType.MATCHING_PAIRS,
                            prompt = "Harflarni ularning nomlari bilan moslang",
                            pairs = mapOf(
                                "د" to "Dal",
                                "ذ" to "Zal",
                                "ر" to "Ro",
                                "ز" to "Za"
                            )
                        ),
                        Exercise(
                            id = "ex_3_3",
                            type = ExerciseType.TYPING,
                            prompt = "'رَأْسٌ' so'zining ma'nosini yozing",
                            arabicText = "رَأْسٌ",
                            correctAnswer = "Bosh",
                            explanation = "رَأْسٌ = Bosh"
                        ),
                        Exercise(
                            id = "ex_3_4",
                            type = ExerciseType.MULTIPLE_CHOICE_UZ_TO_AR,
                            prompt = "'Oltin' so'zi arabchada qanday aytiladi?",
                            uzbekText = "Oltin",
                            options = listOf("ذَهَبٌ", "فِضَّةٌ", "مَالٌ", "شَمْسٌ"),
                            correctAnswer = "ذَهَبٌ",
                            explanation = "ذَهَبٌ (Zahabun) = Oltin"
                        )
                    )
                )
            )
        ),
        LearningUnit(
            id = "unit_2",
            orderIndex = 2,
            title = "2-Bo'lim: Qisqa Unlilar va Harakatlar",
            description = "Fatha (ـَ), Kasra (ـِ), Damma (ـُ), Sukun (ـْ), Tanvin va Shadda sirlari",
            level = "Boshlang'ich",
            colorHex = "#1CB0F6", // Bold Sky Blue
            lessons = listOf(
                Lesson(
                    id = "u2_l1",
                    unitId = "unit_2",
                    title = "Fatha, Kasra, Damma",
                    description = "Uch asosiy qisqa unli tovush: 'a', 'i', 'u'",
                    orderIndex = 1,
                    xpReward = 20,
                    exercises = listOf(
                        Exercise(
                            id = "ex_h_1",
                            type = ExerciseType.MULTIPLE_CHOICE_AR_TO_UZ,
                            prompt = "Harf ustidagi 'ـَ' belgisi (Fatha) qanday tovush beradi?",
                            arabicText = "بَ",
                            options = listOf("A tovushi", "I tovushi", "U tovushi", "To'xtash"),
                            correctAnswer = "A tovushi",
                            explanation = "Fatha (ـَ) harf ustiga qo'yiladi va qisqa 'A' (ba'zan 'O') tovushini beradi: بَ = Ba"
                        ),
                        Exercise(
                            id = "ex_h_2",
                            type = ExerciseType.MULTIPLE_CHOICE_AR_TO_UZ,
                            prompt = "Harf ostidagi 'ـِ' belgisi (Kasra) qanday o'qiladi?",
                            arabicText = "بِ",
                            options = listOf("I tovushi", "A tovushi", "U tovushi", "E tovushi"),
                            correctAnswer = "I tovushi",
                            explanation = "Kasra (ـِ) harf ostiga qo'yiladi va 'I' tovushini ifodalaydi: بِ = Bi"
                        ),
                        Exercise(
                            id = "ex_h_3",
                            type = ExerciseType.MULTIPLE_CHOICE_AR_TO_UZ,
                            prompt = "Harf ustidagi vergulga o'xshash 'ـُ' belgisi (Damma) qaysi tovush?",
                            arabicText = "بُ",
                            options = listOf("U tovushi", "O tovushi", "I tovushi", "A tovushi"),
                            correctAnswer = "U tovushi",
                            explanation = "Damma (ـُ) qisqa 'U' tovushini bildiradi: بُ = Bu"
                        ),
                        Exercise(
                            id = "ex_h_4",
                            type = ExerciseType.MATCHING_PAIRS,
                            prompt = "Harakatlarni tovushlari bilan moslashtiring",
                            pairs = mapOf(
                                "ـَ (Fatha)" to "A tovushi",
                                "ـِ (Kasra)" to "I tovushi",
                                "ـُ (Damma)" to "U tovushi",
                                "ـْ (Sukun)" to "To'xtash (unlisiz)"
                            )
                        ),
                        Exercise(
                            id = "ex_h_5",
                            type = ExerciseType.LISTENING,
                            prompt = "Tinglang va to'g'ri o'qilishini tanlang: كِتَابٌ",
                            arabicText = "كِتَابٌ",
                            options = listOf("Kitābun", "Kātibun", "Kutubun", "Maktubun"),
                            correctAnswer = "Kitābun",
                            explanation = "كِـ (Ki) + ـتَا (tā) + بٌ (bun) = Kitob"
                        )
                    )
                ),
                Lesson(
                    id = "u2_l2",
                    unitId = "unit_2",
                    title = "Sukun va Tanvin",
                    description = "Sukun (ـْ) unlisizlikni, Tanvin (ـً ـٍ ـٌ) esa so'z oxiridagi 'n' tovushini bildiradi",
                    orderIndex = 2,
                    xpReward = 20,
                    exercises = listOf(
                        Exercise(
                            id = "ex_s_1",
                            type = ExerciseType.MULTIPLE_CHOICE_AR_TO_UZ,
                            prompt = "'ـْ' (Sukun) belgisi qanday vazifani bajaradi?",
                            arabicText = "مَنْ",
                            options = listOf("Harfni unlisiz to'xtatib o'qitadi", "Harfni cho'zadi", "Ikkilantiradi", "Unli tovush qo'shadi"),
                            correctAnswer = "Harfni unlisiz to'xtatib o'qitadi",
                            explanation = "Sukun (ـْ) belgisida harf unlisiz to'xtatiladi. Masalan: مَنْ (Man = Kim)"
                        ),
                        Exercise(
                            id = "ex_s_2",
                            type = ExerciseType.MATCHING_PAIRS,
                            prompt = "Tanvin turlarini o'qilishi bilan moslang",
                            pairs = mapOf(
                                "ـً (Fathatan)" to "-an",
                                "ـٍ (Kasratan)" to "-in",
                                "ـٌ (Dammatan)" to "-un",
                                "ـّ (Shadda)" to "Ikkilantirish"
                            )
                        ),
                        Exercise(
                            id = "ex_s_3",
                            type = ExerciseType.TYPING,
                            prompt = "'قَلَمٌ' so'zining o'zbekcha tarjimasini yozing",
                            arabicText = "قَلَمٌ",
                            correctAnswer = "Qalam",
                            explanation = "قَلَمٌ = Qalam"
                        ),
                        Exercise(
                            id = "ex_s_4",
                            type = ExerciseType.SENTENCE_BUILDER,
                            prompt = "Jumla tuzing: 'Bu qalam'",
                            uzbekText = "Bu qalam",
                            sentenceWords = listOf("هَذَا", "قَلَمٌ"),
                            correctAnswer = "هَذَا قَلَمٌ",
                            explanation = "هَذَا (Bu) + قَلَمٌ (qalam)"
                        )
                    )
                )
            )
        ),
        LearningUnit(
            id = "unit_3",
            orderIndex = 3,
            title = "3-Bo'lim: Salomlashish va Odob So'zlari",
            description = "Kundalik salomlashish, minnatdorchilik va xayrlashuv jumlalari",
            level = "Boshlang'ich",
            colorHex = "#FF9600", // Duolingo Orange
            lessons = listOf(
                Lesson(
                    id = "u3_l1",
                    unitId = "unit_3",
                    title = "Salom va Xush kelibsiz",
                    description = "Marhaban, Ahlan va sahlan, Shukran",
                    orderIndex = 1,
                    xpReward = 20,
                    exercises = listOf(
                        Exercise(
                            id = "ex_sal_1",
                            type = ExerciseType.MULTIPLE_CHOICE_AR_TO_UZ,
                            prompt = "'شُكْرًا' (Shukran) so'zining ma'nosi nima?",
                            arabicText = "شُكْرًا",
                            options = listOf("Rahmat / Tashakkur", "Salom", "Xayr", "Ha"),
                            correctAnswer = "Rahmat / Tashakkur",
                            explanation = "شُكْرًا = Rahmat"
                        ),
                        Exercise(
                            id = "ex_sal_2",
                            type = ExerciseType.MULTIPLE_CHOICE_UZ_TO_AR,
                            prompt = "'Kechirasiz / Arzimaydi' arabchada qanday bo'ladi?",
                            uzbekText = "Arzimaydi",
                            options = listOf("عَفْوًا", "شُكْرًا", "مَرْحَبًا", "نَعَمْ"),
                            correctAnswer = "عَفْوًا",
                            explanation = "عَفْوًا ('Afwan) = Arzimaydi / Kechirasiz"
                        ),
                        Exercise(
                            id = "ex_sal_3",
                            type = ExerciseType.SENTENCE_BUILDER,
                            prompt = "Tashakkur bildiring: 'Sizga katta rahmat!'",
                            uzbekText = "Katta rahmat",
                            sentenceWords = listOf("شُكْرًا", "جَزِيلًا"),
                            correctAnswer = "شُكْرًا جَزِيلًا",
                            explanation = "شُكْرًا جَزِيلًا = Katta rahmat"
                        ),
                        Exercise(
                            id = "ex_sal_4",
                            type = ExerciseType.MATCHING_PAIRS,
                            prompt = "Iboralarni moslashtiring",
                            pairs = mapOf(
                                "صَبَاحُ الخَيْرِ" to "Xayrli tong",
                                "مَسَاءُ الخَيْرِ" to "Xayrli kech",
                                "مَعَ السَّلَامَةِ" to "Xayr / Salomat bo'ling",
                                "مَرْحَبًا" to "Salom"
                            )
                        ),
                        Exercise(
                            id = "ex_sal_5",
                            type = ExerciseType.LISTENING,
                            prompt = "Tinglang va to'g'ri jumlani toping: مَعَ السَّلَامَةِ",
                            arabicText = "مَعَ السَّلَامَةِ",
                            options = listOf("Xayr / Salomat bo'ling", "Xush kelibsiz", "Rahmat", "Ha"),
                            correctAnswer = "Xayr / Salomat bo'ling",
                            explanation = "مَعَ السَّلَامَةِ = Xayr / Salomat bo'ling"
                        )
                    )
                ),
                Lesson(
                    id = "u3_l2",
                    unitId = "unit_3",
                    title = "Hol-ahvol so'rash",
                    description = "Kayfa holuk? Ana bixayr!",
                    orderIndex = 2,
                    xpReward = 20,
                    exercises = listOf(
                        Exercise(
                            id = "ex_hol_1",
                            type = ExerciseType.MULTIPLE_CHOICE_AR_TO_UZ,
                            prompt = "'كَيْفَ حَالُكَ؟' jumlasi qanday tarjima qilinadi?",
                            arabicText = "كَيْفَ حَالُكَ؟",
                            options = listOf("Ahvollaringiz qanday?", "Ismingiz nima?", "Qayerdansiz?", "Qayerga ketyapsiz?"),
                            correctAnswer = "Ahvollaringiz qanday?",
                            explanation = "كَيْفَ حَالُكَ؟ = Ahvollaringiz qanday?"
                        ),
                        Exercise(
                            id = "ex_hol_2",
                            type = ExerciseType.SENTENCE_BUILDER,
                            prompt = "Javob qaytaring: 'Men yaxshiman, Allohga shukr'",
                            uzbekText = "Men yaxshiman",
                            sentenceWords = listOf("أَنَا", "بِخَيْرٍ", "الحَمْدُ", "للهِ"),
                            correctAnswer = "أَنَا بِخَيْرٍ الحَمْدُ للهِ",
                            explanation = "أَنَا بِخَيْرٍ الحَمْدُ للهِ = Men yaxshiman, Allohga shukr"
                        ),
                        Exercise(
                            id = "ex_hol_3",
                            type = ExerciseType.MATCHING_PAIRS,
                            prompt = "Savol va javoblarni juftlang",
                            pairs = mapOf(
                                "كَيْفَ حَالُكَ؟" to "أَنَا بِخَيْرٍ",
                                "السَّلَامُ عَلَيْكُمْ" to "وَعَلَيْكُمُ السَّلَامُ",
                                "صَبَاحُ الخَيْرِ" to "صَبَاحُ النُّورِ",
                                "شُكْرًا" to "عَفْوًا"
                            )
                        )
                    )
                )
            )
        ),
        LearningUnit(
            id = "unit_4",
            orderIndex = 4,
            title = "4-Bo'lim: Oila va Qarindoshlar",
            description = "Ota, ona, aka-uka, opa-singil va yaqinlar haqidagi so'zlar",
            level = "O'rta",
            colorHex = "#CE82FF", // Duolingo Purple
            lessons = listOf(
                Lesson(
                    id = "u4_l1",
                    unitId = "unit_4",
                    title = "Ota-ona va Farzandlar",
                    description = "Ab, Umm, Ibn, Bint",
                    orderIndex = 1,
                    xpReward = 20,
                    exercises = listOf(
                        Exercise(
                            id = "ex_oila_1",
                            type = ExerciseType.MULTIPLE_CHOICE_AR_TO_UZ,
                            prompt = "'أُمٌّ' so'zining ma'nosi nima?",
                            arabicText = "أُمٌّ",
                            options = listOf("Ona", "Ota", "Opa", "Buvim"),
                            correctAnswer = "Ona",
                            explanation = "أُمٌّ (Ummun) = Ona"
                        ),
                        Exercise(
                            id = "ex_oila_2",
                            type = ExerciseType.MATCHING_PAIRS,
                            prompt = "Oila a'zolarini moslang",
                            pairs = mapOf(
                                "أَبٌ" to "Ota",
                                "أُمٌّ" to "Ona",
                                "اِبْنٌ" to "O'g'il farzand",
                                "بِنْتٌ" to "Qiz farzand"
                            )
                        ),
                        Exercise(
                            id = "ex_oila_3",
                            type = ExerciseType.SENTENCE_BUILDER,
                            prompt = "Jumla tuzing: 'Bu mening otam'",
                            uzbekText = "Bu mening otam",
                            sentenceWords = listOf("هَذَا", "أَبِي"),
                            correctAnswer = "هَذَا أَبِي",
                            explanation = "هَذَا أَبِي = Bu mening otam"
                        )
                    )
                )
            )
        ),
        LearningUnit(
            id = "unit_5",
            orderIndex = 5,
            title = "5-Bo'lim: Sonlar va Sanash (1 dan 10 gacha)",
            description = "Arabcha hisoblash, raqamlar va ularning ishlatilishi",
            level = "O'rta",
            colorHex = "#FF4B4B", // Duolingo Ruby Red
            lessons = listOf(
                Lesson(
                    id = "u5_l1",
                    unitId = "unit_5",
                    title = "1 dan 5 gacha sanash",
                    description = "Vohid, Isnani, Salasa, Arba'a, Xamsa",
                    orderIndex = 1,
                    xpReward = 20,
                    exercises = listOf(
                        Exercise(
                            id = "ex_num_1",
                            type = ExerciseType.MULTIPLE_CHOICE_AR_TO_UZ,
                            prompt = "'وَاحِدٌ' soni qaysi?",
                            arabicText = "وَاحِدٌ",
                            options = listOf("1 (Bir)", "2 (Ikki)", "3 (Uch)", "4 (To'rt)"),
                            correctAnswer = "1 (Bir)",
                            explanation = "وَاحِدٌ = Bir (1)"
                        ),
                        Exercise(
                            id = "ex_num_2",
                            type = ExerciseType.MATCHING_PAIRS,
                            prompt = "Sonlarni moslashtiring",
                            pairs = mapOf(
                                "وَاحِدٌ" to "Bir (1)",
                                "اِثْنَانِ" to "Ikki (2)",
                                "ثَلَاثَةٌ" to "Uch (3)",
                                "أَرْبَعَةٌ" to "To'rt (4)",
                                "خَمْسَةٌ" to "Besh (5)"
                            )
                        )
                    )
                )
            )
        )
    )

    val READING_TEXTS: List<ReadingText> = listOf(
        ReadingText(
            id = "story_1",
            title = "فِي المَدْرَسَةِ (Maktabda)",
            level = "Boshlang'ich",
            arabicParagraphs = listOf(
                "هَذِهِ مَدْرَسَتِي الجَمِيلَةُ.",
                "أَنَا طَالِبٌ مُجْتَهِدٌ، أَدْخُلُ الفَصْلَ كُلَّ يَوْمٍ فِي الصَّبَاحِ.",
                "المُعَلِّمُ يَقِفُ أَمَامَ السَّبُّورَةِ وَيَشْرَحُ الدَّرْسَ بِصَوْتٍ وَاضِحٍ.",
                "عَلَى مَكْتَبِي كِتَابٌ وَدَفْتَرٌ وَقَلَمٌ نَظِيفٌ."
            ),
            uzbekTranslation = listOf(
                "Bu mening chiroyli maktabim.",
                "Men tirishqoq talabaman, har kuni ertalab sinfga kiraman.",
                "Ustoz doska oldida turib darsni ravshan ovoz bilan tushuntiradi.",
                "Partam ustida kitob, daftar va toza qalam bor."
            ),
            vocabularyNotes = mapOf(
                "مَدْرَسَةٌ" to "Maktab",
                "طَالِبٌ" to "Talaba",
                "مُعَلِّمٌ" to "O'qituvchi",
                "كِتَابٌ" to "Kitob",
                "قَلَمٌ" to "Qalam",
                "مَكْتَبٌ" to "Parta / Stol"
            )
        ),
        ReadingText(
            id = "story_2",
            title = "أُسْرَتِي السَّعِيدَةُ (Mening Baxtli Oilam)",
            level = "Boshlang'ich",
            arabicParagraphs = listOf(
                "أَنَا أَحْمَدُ، وَهَذِهِ أُسْرَتِي.",
                "أَبِي رَجُلٌ طَيِّبٌ يَعْمَلُ بِجِدٍّ.",
                "أُمِّي تَطْبُخُ الطَّعَامَ اللَّذِيذَ فِي البَيْتِ.",
                "نَجْلِسُ جَمِيعًا مَعًا عَلَى المَائِدَةِ وَنَشْكُرُ اللهَ عَلَى نِعَمِهِ."
            ),
            uzbekTranslation = listOf(
                "Men Ahmadman, bu esa mening oilam.",
                "Otam astoydil ishlaydigan yaxshi inson.",
                "Onam uyda mazali taomlar pishiradi.",
                "Hammamiz birgalikda dasturxon atrofida o'tiramiz va ne'matlari uchun Allohga shukr qilamiz."
            ),
            vocabularyNotes = mapOf(
                "أُسْرَةٌ" to "Oila",
                "أَبٌ" to "Ota",
                "أُمٌّ" to "Ona",
                "بَيْتٌ" to "Uy",
                "طَعَامٌ" to "Taom"
            )
        )
    )
}
