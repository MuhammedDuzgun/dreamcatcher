package com.yapai.dreamcatcher.service.ai;

import com.yapai.dreamcatcher.model.DreamInterpretation;

public interface IDreamService {
    DreamInterpretation getDreamInterpretation(String dream);
}
