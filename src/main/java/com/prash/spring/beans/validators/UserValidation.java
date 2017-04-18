package com.prash.spring.beans.validators;

import static com.prash.spring.beans.validators.ValidationResult.invalid;
import static com.prash.spring.beans.validators.ValidationResult.valid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.util.StringUtils;

import com.prash.spring.beans.User;

public interface UserValidation extends Function<User, ValidationResult> {

	static UserValidation firstNameIsNotEmpty() {
		return holds(user -> !StringUtils.isEmpty(user.getFirstName()), ErrorEnum.FIELDEMPTYFIRSTNAME);
	}

	static UserValidation lastNameIsNotEmpty() {
		return holds(user -> !StringUtils.isEmpty(user.getLastName()), ErrorEnum.FIELDEMPTYLASTNAME);
	}

	static UserValidation checkAgeNSalary() {
		return holds(user -> user.getAge() < 18 && user.getSalary() < 20000.0d, ErrorEnum.USERUNDERAGE);
	}

	static UserValidation holds(Predicate<User> p, ErrorEnum error) {
		return user -> p.test(user) ? valid() : invalid(error);
	}

	static UserValidation all(List<ErrorEnum> results, UserValidation... validations) {
		return user -> {
			Arrays.stream(validations).forEach(v -> v.apply(user).getReason().ifPresent(e -> results.add(e)));
			return null;
		};
	}
	
	static List<ErrorEnum> allv2(User user, UserValidation... validations) {
		List<ErrorEnum> results = new ArrayList<>();
		Arrays.stream(validations).forEach(v -> v.apply(user).getReason().ifPresent(e -> results.add(e)));
		return results;
	}

	public default UserValidation and(UserValidation other) {
		return user -> {
			final ValidationResult result = this.apply(user);
			return result.isValid() ? other.apply(user) : result;
		};
	}
}
