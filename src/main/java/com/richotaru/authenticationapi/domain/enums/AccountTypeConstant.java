package com.richotaru.authenticationapi.domain.enums;

import java.util.*;

public enum AccountTypeConstant {
        CLIENT_SYSTEM("CLIENT_SYSTEM"),
        CLIENT_USER("CLIENT_USER");

        private static final long serialVersionUID = -9086148061317024860L;
        private final String enumValue;
        private static Map<String, AccountTypeConstant> values = new LinkedHashMap(3, 1.0F);
        private static List<String> literals = new ArrayList(3);
        private static List<String> names = new ArrayList(3);
        private static List<AccountTypeConstant> valueList = new ArrayList(3);

        private AccountTypeConstant(String value) {
                this.enumValue = value;
        }

        public static AccountTypeConstant fromString(String name) {
                return valueOf(name);
        }

        public String value() {
                return this.enumValue;
        }

        public static AccountTypeConstant fromValue(String value) {
                AccountTypeConstant[] var1 = values();
                int var2 = var1.length;

                for(int var3 = 0; var3 < var2; ++var3) {
                        AccountTypeConstant enumName = var1[var3];
                        if (enumName.getValue().equals(value)) {
                                return enumName;
                        }
                }

                throw new IllegalArgumentException("AccountTypeConstant.fromValue(" + value + ')');
        }

        public String getValue() {
                return this.enumValue;
        }

        public static List<String> literals() {
                return literals;
        }

        public static List<String> names() {
                return names;
        }

        static {
                synchronized(values) {
                        values.put(CLIENT_SYSTEM.enumValue, CLIENT_SYSTEM);
                        values.put(CLIENT_USER.enumValue, CLIENT_USER);
                }

                synchronized(valueList) {
                        valueList.add(CLIENT_SYSTEM);
                        valueList.add(CLIENT_USER);
                        valueList = Collections.unmodifiableList(valueList);
                }

                synchronized(literals) {
                        literals.add(CLIENT_SYSTEM.enumValue);
                        literals.add(CLIENT_USER.enumValue);
                        literals = Collections.unmodifiableList(literals);
                }

                synchronized(names) {
                        names.add("CLIENT_SYSTEM");
                        names.add("CLIENT_USER");
                        names = Collections.unmodifiableList(names);
                }
        }
}
