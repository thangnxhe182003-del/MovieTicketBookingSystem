        </div>
    </main>

    <script>
        // Mobile sidebar toggle
        function toggleSidebar() {
            document.querySelector('.admin-sidebar').classList.toggle('open');
        }

        // Auto-hide alerts
        document.addEventListener('DOMContentLoaded', function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                setTimeout(() => {
                    alert.style.opacity = '0';
                    setTimeout(() => alert.remove(), 300);
                }, 5000);
            });
        });

        // Confirm delete actions
        function confirmDelete(message) {
            return confirm(message || 'Bạn có chắc chắn muốn xóa mục này?');
        }

        // Form validation
        function validateForm(formId) {
            const form = document.getElementById(formId);
            if (!form) return false;
            
            const requiredFields = form.querySelectorAll('[required]');
            let isValid = true;
            
            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    field.style.borderColor = '#e50914';
                    isValid = false;
                } else {
                    field.style.borderColor = '#ddd';
                }
            });
            
            return isValid;
        }
    </script>
</body>
</html>
