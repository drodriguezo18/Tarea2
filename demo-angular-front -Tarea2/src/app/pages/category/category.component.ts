import { Component, effect, inject } from '@angular/core';
import { PaginationComponent } from '../../components/pagination/pagination.component';
import { CategoryListService } from '../../services/category-list.service';
import { LoaderComponent } from '../../components/loader/loader.component';
import { CategoryListComponent } from '../../components/category/category-list/category-list.component';
import { CategoryListFormComponent } from '../../components/category/category-list-form/category-list-form.component';
import { FormBuilder, Validators } from '@angular/forms';
import { ICategoryList } from '../../interfaces';

@Component({
  selector: 'app-category',
  standalone: true,
  imports: [
    PaginationComponent,
    LoaderComponent,
    CategoryListComponent,
    CategoryListFormComponent
  ],
  templateUrl: './category.component.html',
  styleUrl: './category.component.scss'
})
export class CategoryComponent {
  public categoryListService: CategoryListService = inject(CategoryListService);
  public fb: FormBuilder = inject(FormBuilder);
  public form = this.fb.group({
    id: [0],
    name: ['', Validators.required],
    description: ['', Validators.required]
  })
  constructor() {
    this.categoryListService.getAll();
    effect(() => {
      console.log('category lists updated', this.categoryListService.categoriesLists$());
      if (this.categoryListService.categoriesLists$()[0]) {
        this.categoryListService.categoriesLists$()[0] ?  this.categoryListService.categoriesLists$()[0].name = `${this.categoryListService.categoriesLists$()[0].name} - Caros` : null;
      }
    });
  }

  save(item: ICategoryList) {
    item.id ? this.categoryListService.update(item) : this.categoryListService.save(item);
    this.form.reset();
  }

  delete(item: ICategoryList) {
    console.log('delete', item);
    this.categoryListService.delete(item);
  }
}
