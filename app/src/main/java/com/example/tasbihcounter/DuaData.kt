package com.example.tasbihcounter

object DuaData {

    val categories = listOf(

        // 1️⃣ Five Kalimaat
        DuaCategory(
            name = "Five Kalimaat / Iman Mujmal / Mufassal",
            duas = listOf(

                Dua(
                    title = "1️⃣ Kalima Tayyibah",
                    arabic = "لَا إِلٰهَ إِلَّا اللّٰهُ مُحَمَّدٌ رَّسُولُ اللّٰهِ",
                    transliteration = "La ilaha illallah Muhammadur Rasulullah",
                    meaning = "There is no god but Allah, Muhammad is the Messenger of Allah.",
                    reference = "Declaration of Faith"
                ),

                Dua(
                    title = "2️⃣ Kalima Shahadat",
                    arabic = "أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللّٰهُ وَحْدَهُ لَا شَرِيكَ لَهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ",
                    transliteration = "Ashhadu an la ilaha illallahu wahdahu la sharika lahu wa ashhadu anna Muhammadan abduhu wa rasuluhu",
                    meaning = "I bear witness that there is no god but Allah, He is One and has no partner, and Muhammad is His servant and messenger.",
                    reference = "Declaration of Testimony"
                ),

                Dua(
                    title = "3️⃣ Kalima Tamjeed",
                    arabic = "سُبْحَانَ اللّٰهِ وَالْحَمْدُ لِلّٰهِ وَلَا إِلٰهَ إِلَّا اللّٰهُ وَاللّٰهُ أَكْبَرُ",
                    transliteration = "Subhanallahi walhamdulillahi wa la ilaha illallahu wallahu akbar",
                    meaning = "Glory be to Allah, all praise be to Allah, there is no god but Allah, Allah is the Greatest.",
                    reference = "Tasbeeh & Tahmeed"
                ),

                Dua(
                    title = "4️⃣ Kalima Tawheed",
                    arabic = "لَا إِلٰهَ إِلَّا اللّٰهُ وَحْدَهُ لَا شَرِيكَ لَهُ لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ يُحْيِي وَيُمِيتُ",
                    transliteration = "La ilaha illallahu wahdahu la sharika lahu, lahul mulku walahul hamdu yuhyi wa yumeet",
                    meaning = "There is no god but Allah alone. To Him belongs the kingdom and all praise. He gives life and death.",
                    reference = "Oneness of Allah"
                ),

                Dua(
                    title = "5️⃣ Kalima Radd-e-Kufr",
                    arabic = "اللّٰهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ أَنْ أُشْرِكَ بِكَ شَيْئًا وَأَنَا أَعْلَمُ بِهِ وَأَسْتَغْفِرُكَ لِمَا لَا أَعْلَمُ بِهِ",
                    transliteration = "Allahumma inni a'udhu bika min an ushrika bika shay'an wa ana a'lamu bihi wa astaghfiruka lima la a'lamu bihi",
                    meaning = "O Allah, I seek refuge in You from associating partners with You knowingly and I seek forgiveness for what I do unknowingly.",
                    reference = "Protection from Shirk"
                ),

                Dua(
                    title = "Iman Mujmal",
                    arabic = "آمَنْتُ بِاللّٰهِ كَمَا هُوَ بِأَسْمَائِهِ وَصِفَاتِهِ وَقَبِلْتُ جَمِيعَ أَحْكَامِهِ",
                    transliteration = "Amantu billahi kama huwa bi asma'ihi wa sifatihi wa qabiltu jami'a ahkamihi",
                    meaning = "I believe in Allah as He is with His names and attributes and I accept all His commands.",
                    reference = "Traditional Aqeedah"
                ),

                Dua(
                    title = "Iman Mufassal",
                    arabic = "آمَنْتُ بِاللّٰهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ...",
                    transliteration = "Amantu billahi wa malaikatihi wa kutubihi wa rusulihi...",
                    meaning = "I believe in Allah, His angels, His books, His messengers...",
                    reference = "Six Articles of Faith"
                )
            )
        ),


        // 2️⃣ Eating & Drinking
        DuaCategory(
            name = "Prayers For Eating And Drinking",
            duas = listOf(
                Dua(
                    title = "Before Eating",
                    arabic = "بِسْمِ اللَّهِ",
                    transliteration = "Bismillah",
                    meaning = "In the name of Allah.",
                    reference = "Abu Dawood 3767"
                ),
                Dua(
                    title = "After Eating",
                    arabic = "الْحَمْدُ لِلَّهِ الَّذِي أَطْعَمَنَا...",
                    transliteration = "Alhamdulillahilladhi at'amana...",
                    meaning = "All praise is due to Allah who fed us and provided for us.",
                    reference = "Tirmidhi 3458"
                )
            )
        ),

        // 3️⃣ Toilet
        DuaCategory(
            name = "Prayers For Toilet",
            duas = listOf(
                Dua(
                    title = "Entering Toilet",
                    arabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْخُبُثِ...",
                    transliteration = "Allahumma inni a'udhu bika...",
                    meaning = "O Allah, I seek refuge in You from impurity.",
                    reference = "Bukhari 142"
                ),
                Dua(
                    title = "Leaving Toilet",
                    arabic = "غُفْرَانَكَ",
                    transliteration = "Ghufranak",
                    meaning = "I seek Your forgiveness.",
                    reference = "Abu Dawood 30"
                )
            )
        ),

        // 4️⃣ Sleeping & Waking
        DuaCategory(
            name = "Prayers From Sleeping To Waking",
            duas = listOf(
                Dua(
                    title = "Before Sleeping",
                    arabic = "بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا",
                    transliteration = "Bismika Allahumma amootu wa ahya",
                    meaning = "In Your name O Allah, I die and I live.",
                    reference = "Bukhari 6324"
                ),
                Dua(
                    title = "After Waking",
                    arabic = "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا...",
                    transliteration = "Alhamdulillahilladhi ahyana...",
                    meaning = "All praise is for Allah who gave us life after death.",
                    reference = "Bukhari 6312"
                )
            )
        ),

        // 5️⃣ Clothes
        DuaCategory(
            name = "Prayers About Clothes",
            duas = listOf(
                Dua(
                    title = "Wearing New Clothes",
                    arabic = "اللَّهُمَّ لَكَ الْحَمْدُ...",
                    transliteration = "Allahumma lakal hamd...",
                    meaning = "O Allah, all praise is Yours for clothing me with this.",
                    reference = "Abu Dawood 4020"
                )
            )
        ),

        // 6️⃣ Entering & Leaving Home
        DuaCategory(
            name = "Prayers To Enter And Exit Home",
            duas = listOf(
                Dua(
                    title = "Entering Home",
                    arabic = "بِسْمِ اللَّهِ وَلَجْنَا...",
                    transliteration = "Bismillahi walajna...",
                    meaning = "In the name of Allah we enter.",
                    reference = "Abu Dawood 5096"
                ),
                Dua(
                    title = "Leaving Home",
                    arabic = "بِسْمِ اللَّهِ تَوَكَّلْتُ عَلَى اللَّهِ...",
                    transliteration = "Bismillah tawakkaltu ala Allah...",
                    meaning = "In the name of Allah, I trust in Allah.",
                    reference = "Abu Dawood 5095"
                )
            )
        ),

        // 7️⃣ Morning & Evening
        DuaCategory(
            name = "Prayers For Morning And Evening",
            duas = listOf(
                Dua(
                    title = "Morning Protection",
                    arabic = "اللَّهُمَّ بِكَ أَصْبَحْنَا...",
                    transliteration = "Allahumma bika asbahna...",
                    meaning = "O Allah, by You we enter the morning.",
                    reference = "Abu Dawood 5071"
                ),
                Dua(
                    title = "Evening Protection",
                    arabic = "اللَّهُمَّ بِكَ أَمْسَيْنَا...",
                    transliteration = "Allahumma bika amsayna...",
                    meaning = "O Allah, by You we enter the evening.",
                    reference = "Abu Dawood 5073"
                )
            )
        ),

        // 8️⃣ After Salah
        DuaCategory(
            name = "Prayers After Salaat",
            duas = listOf(
                Dua(
                    title = "Astaghfirullah x3",
                    arabic = "أَسْتَغْفِرُ اللَّهَ",
                    transliteration = "Astaghfirullah",
                    meaning = "I seek forgiveness from Allah.",
                    reference = "Muslim 591"
                )
            )
        ),

        // 9️⃣ Tahajjud
        DuaCategory(
            name = "Prayers Related To Tahajjud Salaat",
            duas = listOf(
                Dua(
                    title = "Tahajjud Opening Dua",
                    arabic = "اللَّهُمَّ لَكَ الْحَمْدُ أَنْتَ نُورُ السَّمَاوَاتِ...",
                    transliteration = "Allahumma lakal hamd anta nurus samawati...",
                    meaning = "O Allah, to You belongs all praise, You are the Light of the heavens.",
                    reference = "Bukhari 1120"
                )
            )
        ),

        // 🔟 Knowledge
        DuaCategory(
            name = "Prayers Related To Knowledge",
            duas = listOf(
                Dua(
                    title = "Increase Knowledge",
                    arabic = "رَبِّ زِدْنِي عِلْمًا",
                    transliteration = "Rabbi zidni ilma",
                    meaning = "My Lord, increase me in knowledge.",
                    reference = "Qur'an 20:114"
                )
            )
        ),

// 11️⃣ Great Names Of Allah
        DuaCategory(
            name = "Prayers Related To The Great Names Of Allah",
            duas = listOf(
                Dua(
                    title = "Calling Allah By His Greatest Name",
                    arabic = "اللَّهُمَّ إِنِّي أَسْأَلُكَ بِأَنَّ لَكَ الْحَمْدَ...",
                    transliteration = "Allahumma inni as’aluka bi annalaka al-hamd...",
                    meaning = "O Allah, I ask You by the fact that all praise belongs to You...",
                    reference = "Tirmidhi 3475"
                )
            )
        ),

// 12️⃣ Ramadan
        DuaCategory(
            name = "Prayers For Ramadan Al Mubarak",
            duas = listOf(
                Dua(
                    title = "Dua At Iftar",
                    arabic = "اللَّهُمَّ إِنِّي لَكَ صُمْتُ وَبِكَ آمَنْتُ...",
                    transliteration = "Allahumma inni laka sumtu wa bika aamantu...",
                    meaning = "O Allah, I fasted for You and I believe in You.",
                    reference = "Abu Dawood 2358"
                )
            )
        ),

// 13️⃣ Travelling
        DuaCategory(
            name = "Prayers Related To Travelling",
            duas = listOf(
                Dua(
                    title = "Travel Dua",
                    arabic = "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا...",
                    transliteration = "Subhanalladhi sakhkhara lana hatha...",
                    meaning = "Glory is to Him who has subjected this to us.",
                    reference = "Muslim 1342"
                )
            )
        ),

// 14️⃣ Hajj
        DuaCategory(
            name = "Prayers Related To Hajj Pilgrimage",
            duas = listOf(
                Dua(
                    title = "Talbiyah",
                    arabic = "لَبَّيْكَ اللَّهُمَّ لَبَّيْكَ...",
                    transliteration = "Labbayk Allahumma labbayk...",
                    meaning = "Here I am, O Allah, here I am.",
                    reference = "Bukhari 1549"
                )
            )
        ),

// 15️⃣ Forgiveness
        DuaCategory(
            name = "Prayers For Forgiveness",
            duas = listOf(
                Dua(
                    title = "Sayyidul Istighfar",
                    arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ...",
                    transliteration = "Allahumma anta Rabbi la ilaha illa anta...",
                    meaning = "O Allah, You are my Lord, none has the right to be worshipped except You.",
                    reference = "Bukhari 6306"
                )
            )
        ),

// 16️⃣ Faith
        DuaCategory(
            name = "Prayers Related To Faith",
            duas = listOf(
                Dua(
                    title = "Firmness In Faith",
                    arabic = "يَا مُقَلِّبَ الْقُلُوبِ ثَبِّتْ قَلْبِي عَلَى دِينِكَ",
                    transliteration = "Ya Muqallibal quloob thabbit qalbi ala deenik",
                    meaning = "O Turner of hearts, keep my heart firm upon Your religion.",
                    reference = "Tirmidhi 2140"
                )
            )
        ),

// 17️⃣ Protection
        DuaCategory(
            name = "Prayers For Protection From Evil Attributes",
            duas = listOf(
                Dua(
                    title = "Protection From Evil Character",
                    arabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ مُنْكَرَاتِ الْأَخْلَاقِ...",
                    transliteration = "Allahumma inni a’udhu bika min munkaratil akhlaq...",
                    meaning = "O Allah, I seek refuge in You from evil character.",
                    reference = "Tirmidhi 3591"
                )
            )
        ),

// 18️⃣ Children
        DuaCategory(
            name = "Prayers For Children",
            duas = listOf(
                Dua(
                    title = "Righteous Offspring",
                    arabic = "رَبِّ هَبْ لِي مِنَ الصَّالِحِينَ",
                    transliteration = "Rabbi hab li minas saliheen",
                    meaning = "My Lord, grant me from among the righteous.",
                    reference = "Qur'an 37:100"
                )
            )
        )


    )
}