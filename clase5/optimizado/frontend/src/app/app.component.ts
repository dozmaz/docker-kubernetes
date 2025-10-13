import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface Producto {
  id?: number;
  nombre: string;
  precio: number;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Spring Boot API';
  productos: Producto[] = [];
  loading = false;
  error: string | null = null;
  success: string | null = null;

  newProducto: Producto = {
    nombre: '',
    precio: 0
  };

  private apiUrl = 'http://localhost:8080/api/productos';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadProductos();
  }

  loadProductos(): void {
    this.loading = true;
    this.error = null;

    this.http.get<Producto[]>(this.apiUrl).subscribe({
      next: (data) => {
        this.productos = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar usuarios: ' + err.message;
        this.loading = false;
      }
    });
  }

  createProducto(): void {
    if (!this.newProducto.nombre || !this.newProducto.precio) {
      this.error = 'Por favor completa todos los campos';
      return;
    }

    this.loading = true;
    this.error = null;
    this.success = null;

    this.http.post<Producto>(this.apiUrl, this.newProducto).subscribe({
      next: (user) => {
        this.success = 'Producto creado correctamente';
        this.productos.push(user);
        this.newProducto = { nombre: '', precio: 0 };
        this.loading = false;

        // Clear success message after 3 seconds
        setTimeout(() => this.success = null, 3000);
      },
      error: (err) => {
        this.error = 'Error al crear producto: ' + (err.error?.error || err.message);
        this.loading = false;
      }
    });
  }

  deleteProducto(id: number): void {
    if (!confirm('¿Estás seguro de eliminar este producto?')) {
      return;
    }

    this.loading = true;
    this.error = null;
    this.success = null;

    this.http.delete(`${this.apiUrl}/${id}`).subscribe({
      next: () => {
        this.success = 'Producto eliminado correctamente';
        this.productos = this.productos.filter(u => u.id !== id);
        this.loading = false;

        // Clear success message after 3 seconds
        setTimeout(() => this.success = null, 3000);
      },
      error: (err) => {
        this.error = 'Error al eliminar producto: ' + err.message;
        this.loading = false;
      }
    });
  }

  clearMessages(): void {
    this.error = null;
    this.success = null;
  }
}
