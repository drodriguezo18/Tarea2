import { Component, effect, inject, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CategoryService } from '../../services/category.service';
import { CategoryListService } from '../../services/category-list.service';
import { ICategory } from '../../interfaces';
import { CategoryFormComponent } from '../../components/category/category-form/category-form.component';
import { CategoriesTableComponent } from '../../components/category/categories-table/categories-table.component';
import { LoaderComponent } from '../../components/loader/loader.component';
import { PaginationComponent } from '../../components/pagination/pagination.component';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [
    CommonModule,
    CategoryFormComponent,
    CategoriesTableComponent,
    LoaderComponent,
    PaginationComponent
  ],
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.scss'
})
export class CategoriesComponent implements OnInit {
  public categoryService: CategoryService = inject(CategoryService);
  public categoryListService: CategoryListService = inject(CategoryListService);
  public fb: FormBuilder = inject(FormBuilder);
  public areActionsAvailable: boolean = false;
  public authService: AuthService = inject(AuthService);
  public route: ActivatedRoute = inject(ActivatedRoute);
  
  public isEdit: boolean = false;
  
  public form = this.fb.group({
    id: [0],
    name: ['', Validators.required],
    description: [''],
    categoryListId: ['', Validators.required]
  });

  constructor() {
    effect(() => {
      console.log('categories updated', this.categoryService.categories$());
    });
  }

  ngOnInit() {
    this.categoryListService.getAll();
    this.categoryService.getAll();
    this.route.data.subscribe( data => {
      this.areActionsAvailable = this.authService.areActionsAvailable(data['authorities'] ? data['authorities'] : []);
      console.log('areActionsAvailable', this.areActionsAvailable);
    });
  }

  save(category: ICategory) {
    const categoryListId = this.form.get('categoryListId')?.value;
    
    if (this.isEdit && category.id) {
      category.categoryList = { id: Number(categoryListId) };
      this.categoryService.update(category);
    } else {
      if (categoryListId) {
        this.categoryService.addCategoryToCategoryList(Number(categoryListId), category);
      }
    }
    this.form.reset();
    this.isEdit = false;
  }

  edit(category: ICategory) {
    this.isEdit = true;
    this.form.patchValue({
      id: category.id || 0,
      name: category.name || '',
      description: category.description || '',
      categoryListId: category.categoryList?.id?.toString() || ''
    });
  }

  delete(category: ICategory) {
    if (category.id && category.categoryList?.id) {
      this.categoryService.deleteCategoryFromCategoryList(category.categoryList.id, category.id);
    } else if (category.id) {
      this.categoryService.delete(category);
    }
  }
}
