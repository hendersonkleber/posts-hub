import { Component, computed, input } from '@angular/core';
import { ValidationError } from '@angular/forms/signals';

@Component({
  selector: 'app-form-field-error',
  templateUrl: './form-field-error.html',
})
export class FormFieldError {
  public readonly errors = input.required<ValidationError.WithFieldTree[]>();

  protected readonly messages = computed(() => {
    return this.toErrorMessages(this.errors());
  });

  toErrorMessages(errors: ValidationError.WithFieldTree[]) {
    return errors
      .filter(error => error.fieldTree().touched() && error.fieldTree().invalid())
      .map(error => error.message ?? 'Validation error');
  }
}
