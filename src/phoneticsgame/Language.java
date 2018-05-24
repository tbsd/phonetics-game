package phoneticsgame;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Language {
    public static final String NONE = "";
    public static final String GERMAN = "Немецкий";
    public final static ArrayList<String> availableLanguages = new ArrayList(Arrays.asList(GERMAN)); // Insert new languages here

    static LinkedList<Sound> getLanguage(String language) {
        LinkedList<Sound> result = new LinkedList<>();
        switch (language) {         // Insert new languages here
            case NONE:
                break;
            case GERMAN:
                result = getGerman();
                break;
            default:
                throw new IllegalArgumentException();
        }
        return result;
    }

    private static LinkedList<Sound> getGerman() {
        LinkedList<Sound> result;
        try {
            result = (LinkedList<Sound>) IOManager.deserialize(new File(IOManager.LANGUAGESDIR + "german.lang"));
        } catch (Exception readException) {
            result = new LinkedList<>(Arrays.asList(
                    // With additional separation on short and long vowels
//                    new Sound(new File(IOManager.SOUNDDIR +
//                            ".wav").getPath(), "a"), // /a/
//                    new Sound(new File(IOManager.SOUNDDIR +
//                            "Open_front_unrounded_vowel.wav").getPath(), "a aa ah"), // /a:/
//                    new Sound(new File(IOManager.SOUNDDIR +
//                            ".wav").getPath(), "e ä"), // /ε/
//                    new Sound(new File(IOManager.SOUNDDIR +
//                            "Open-mid_front_unrounded_vowel.wav").getPath(), "ä äh"), // /ε:/
//                    new Sound(new File(IOManager.SOUNDDIR +
//                            ".wav").getPath(), "е"), // /ə/
//                    new Sound(new File(IOManager.SOUNDDIR +
//                            "Mid-central_vowel.wav").getPath(), "e ee eh"), // /e:/
//                    new Sound(new File(IOManager.SOUNDDIR +
//                            ".wav").getPath(), "i"), // /ı/
//                    new Sound(new File(IOManager.SOUNDDIR +
//                            "Close_front_unrounded_vowel.wav").getPath(), "i ieh ie ih"), // /i:/

                    // Without short versions of some vowels
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Open_front_unrounded_vowel.wav").getPath(), "a aa ah"), // /a/ /a:/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Open-mid_front_unrounded_vowel.wav").getPath(), "e ä äh"), // /ε/ /ε:/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Mid-central_vowel.wav").getPath(), "е ee eh"), // /ə/ /e:/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Close_front_unrounded_vowel.wav").getPath(), "i ieh ie ih"), // /ı/ /i:/

                    new Sound(new File(IOManager.SOUNDDIR +
                            "Open-mid_back_rounded_vowel.wav").getPath(), "о"), // /ɔ/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Close-mid_back_rounded_vowel.wav").getPath(), "o oo oh"), // /o:/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Open-mid_front_rounded_vowel.wav").getPath(), "ö"), // /œ/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Close-mid_front_rounded_vowel.wav").getPath(), "ö öh oe"), // /ø:/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Near-close_near-back_rounded_vowel.wav").getPath(), "u"), // /ʊ/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Close_back_rounded_vowel.wav").getPath(), "u uh"), // /u:/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Near-close_near-front_rounded_vowel.wav").getPath(), "ü y"), // /ʏ/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Close_front_rounded_vowel.wav").getPath(), "ü üh y"), // /y:/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiceless_bilabial_plosive.wav").getPath(), "p pp b"), // /p/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiced_bilabial_plosive.wav").getPath(), "b bb"), // /b/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiceless_alveolar_plosive.wav").getPath(), "t tt th d"), // /t/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiced_alveolar_plosive.wav").getPath(), "d dd"), // /d/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiceless_velar_plosive.wav").getPath(), "k ck ch g"), // /k/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiced_velar_plosive_02.wav").getPath(), "g gg"), // /g/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiceless_labiodental_fricative.wav").getPath(), "f ff v ph"), // /f/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiced_labiodental_fricative.wav").getPath(), "w v"), // /v/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiceless_alveolar_sibilant.wav").getPath(), "s ss ß"), // /s/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiced_alveolar_sibilant.wav").getPath(), "s"), // /z/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiceless_palato-alveolar_sibilant.wav").getPath(), "sch s ch"), // /ʃ/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiced_palato-alveolar_sibilant.wav").getPath(), "g j"), // /ʒ/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Palatal_approximant.wav").getPath(), "j y"), // /j/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiceless_palatal_fricative.wav").getPath(), "ch g"), // /ç/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiceless_velar_fricative.wav").getPath(), "ch"), // /x/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiceless_glottal_fricative.wav").getPath(), "h"), // /h/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiced_uvular_fricative.wav").getPath(), "r rr rh"), // /ʁ/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Near-open_central_unrounded_vowel.wav").getPath(), "r er"), // /ɐ/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Alveolar_lateral_approximant.wav").getPath(), "l ll"), // /l/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Bilabial_nasal.wav").getPath(), "m mm"), // /m/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Alveolar_nasal.wav").getPath(), "n nn"), // /n/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Velar_nasal.wav").getPath(), "ng n"), // /ŋ/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiceless_labiodental_affricate.wav").getPath(), "pf"), // /pf/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiceless_alveolar_sibilant_affricate.wav").getPath(), "tz z c t"), // /ts/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiceless_palato-alveolar_affricate.wav").getPath(), "tsch ch tch"), // /tʃ/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Voiced_palato-alveolar_affricate.wav").getPath(), "j"), // /dʒ/
                    new Sound(new File(IOManager.SOUNDDIR +
                            "Near-close_near-front_unrounded_vowel.wav").getPath(), "i") // /ɪ/
            ));
            try {
                IOManager.serialize(result, new File(IOManager.LANGUAGESDIR + "german.lang"));
            } catch (Exception writeException) {
                System.err.println(writeException.toString());
            }
        }
        return result;
    }
}
