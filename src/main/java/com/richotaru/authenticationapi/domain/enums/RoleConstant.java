package com.richotaru.authenticationapi.domain.enums;

import java.util.*;
import java.util.stream.Collectors;

public enum RoleConstant {
        SYSTEM_ADMIN("SYSTEM_ADMIN"),
        ADMIN("ADMIN"),
        USER("USER");

        private static final long serialVersionUID = -9086148061317024860L;
        private final String enumValue;
        private static Map<String, RoleConstant> values = new LinkedHashMap(3, 1.0F);
        private static List<String> literals = new ArrayList(3);
        private static List<String> names = new ArrayList(3);
        private static List<RoleConstant> valueList = new ArrayList(3);

        private RoleConstant(String value) {
                this.enumValue = value;
        }

        public static RoleConstant fromString(String name) {
                return valueOf(name);
        }

        public String value() {
                return this.enumValue;
        }

        public static RoleConstant fromValue(String value) {
                RoleConstant[] var1 = values();
                int var2 = var1.length;

                for(int var3 = 0; var3 < var2; ++var3) {
                        RoleConstant enumName = var1[var3];
                        if (enumName.getValue().equals(value)) {
                                return enumName;
                        }
                }

                throw new IllegalArgumentException("RoleConstant.fromValue(" + value + ')');
        }

        public String getValue() {
                return this.enumValue;
        }

        public static String getValidRolesAsString(String roles){
                return Arrays.stream(roles.split(",")).map(role->{
                        RoleConstant constant = RoleConstant.USER;
                        try{
                                constant = RoleConstant.fromValue(role);
                        }catch (Exception e){
                                e.printStackTrace();
                        }
                        return constant;
                }).distinct().map(RoleConstant::value).collect(Collectors.joining(","));
        }
        public static Set<RoleConstant> getValidRolesAsSet(String roles){
                return Arrays.stream(roles.split(",")).map(role->{
                        RoleConstant constant = RoleConstant.USER;
                        try{
                                constant = RoleConstant.fromValue(role);
                        }catch (Exception e){
                                e.printStackTrace();
                        }
                        return constant;
                }).collect(Collectors.toSet());
        }
        public static String getValidRolesAsString(Set<RoleConstant> valueList){
                return valueList.stream().map(RoleConstant::value).collect(Collectors.joining(","));
        }
        public static List<String> literals() {
                return literals;
        }

        public static List<String> names() {
                return names;
        }

        static {
                synchronized(values) {
                        values.put(SYSTEM_ADMIN.enumValue, SYSTEM_ADMIN);
                        values.put(ADMIN.enumValue, ADMIN);
                        values.put(USER.enumValue, USER);
                }

                synchronized(valueList) {
                        valueList.add(SYSTEM_ADMIN);
                        valueList.add(ADMIN);
                        valueList.add(USER);
                        valueList = Collections.unmodifiableList(valueList);
                }

                synchronized(literals) {
                        literals.add(SYSTEM_ADMIN.enumValue);
                        literals.add(ADMIN.enumValue);
                        literals.add(USER.enumValue);
                        literals = Collections.unmodifiableList(literals);
                }

                synchronized(names) {
                        names.add("SYSTEM_ADMIN");
                        names.add("ADMIN");
                        names.add("USER");
                        names = Collections.unmodifiableList(names);
                }
        }
}
