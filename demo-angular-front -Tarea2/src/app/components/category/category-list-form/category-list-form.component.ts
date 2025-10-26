import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ICategoryList } from '../../../interfaces';

@Component({
  selector: 'app-category-list-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './category-list-form.component.html',
  styleUrl: './category-list-form.component.scss'
})
export class CategoryListFormComponent {
  @Input() form!: FormGroup;
  @Output() callSaveMethod: EventEmitter<ICategoryList> = new EventEmitter<ICategoryList>();
}
