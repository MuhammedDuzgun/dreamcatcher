package com.yapai.dreamcatcher.service.ai;

import com.yapai.dreamcatcher.model.DreamInterpretation;

public interface IDreamServiceAI {
    DreamInterpretation getDreamInterpretation(String dream);
}
