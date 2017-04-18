package com.prash.spring.beans.validators;

import java.util.Optional;

public interface ValidationResult {

	static ValidationResult valid() {
		return ValidationSupport.valid();
	}

	static ValidationResult invalid(ErrorEnum error) {
		return new Invalid(error);
	}

	boolean isValid();

	Optional<ErrorEnum> getReason();

	static class Invalid implements ValidationResult {

		private final ErrorEnum error;

		Invalid(ErrorEnum error) {
			this.error = error;
		}

		public boolean isValid() {
			return false;
		}

		public Optional<ErrorEnum> getReason() {
			return Optional.of(error);
		}

		// equals and hashCode on field reason
	}

	final class ValidationSupport {
		private static final ValidationResult valid = new ValidationResult() {
			public boolean isValid() {
				return true;
			}

			public Optional<ErrorEnum> getReason() {
				return Optional.empty();
			}
		};

		static ValidationResult valid() {
			return valid;
		}
	}
}