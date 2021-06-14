package chengweiou.universe.andromeda.base.converter;


import com.google.gson.Gson;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import chengweiou.universe.andromeda.model.entity.AccountNew;

@Component
public class LoginAccountConverter implements Converter<String, AccountNew> {
    @Override
    public AccountNew convert(String source) {
        return new Gson().fromJson(source, AccountNew.class);
    }
}