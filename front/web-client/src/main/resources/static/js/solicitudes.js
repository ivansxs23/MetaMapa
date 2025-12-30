document.addEventListener('click', (e) => {
    const btn = e.target.closest('.btn-ver-detalle');
    if (!btn) return;

    const tipo = btn.dataset.tipo; // CREACION | EDICION | ELIMINACION

    const modoCre = document.getElementById('modalModoCreacion');
    const modoEdi = document.getElementById('modalModoEdicion');
    const modoEli = document.getElementById('modalModoEliminacion');

    modoCre.classList.add('d-none');
    modoEdi.classList.add('d-none');
    modoEli.classList.add('d-none');

    const title = document.getElementById('modalHechoTitle');

    if (tipo === 'CREACION') {
        title.textContent = 'Detalle de creación';
        modoCre.classList.remove('d-none');

        document.getElementById('m_titulo').textContent = btn.dataset.nuevoTitulo ?? '-';
        document.getElementById('m_desc').textContent = btn.dataset.nuevoDescripcion ?? '-';
        document.getElementById('m_cat').textContent = btn.dataset.nuevoCategoria ?? '-';
        document.getElementById('m_fecha').textContent = btn.dataset.nuevoFecha ?? '-';

        const lat = btn.dataset.nuevoLatitud ?? '-';
        const lng = btn.dataset.nuevoLongitud ?? '-';
        document.getElementById('m_ubicacion').textContent = `${lat}, ${lng}`;

        const anon = btn.dataset.nuevoAnonimo;
        document.getElementById('m_anon').textContent = (anon === 'true' || anon === 'Sí') ? 'Sí' : 'No';
        return;
    }

    if (tipo === 'EDICION') {
        title.textContent = 'Comparación de edición (Actual vs Nuevo)';
        modoEdi.classList.remove('d-none');

        const tbody = document.getElementById('diffBody');
        tbody.innerHTML = '';

        const campos = [
            ['Título', 'titulo'],
            ['Descripción', 'descripcion'],
            ['Fecha', 'fecha'],
            ['Categoría', 'categoria'],
            ['Latitud', 'latitud'],
            ['Longitud', 'longitud'],
            ['Anónimo', 'anonimo']
        ];

        const normalizar = v => (v === null || v === undefined || v === '') ? '-' : v;

        campos.forEach(([label, key]) => {
            addDiffRow(
                tbody,
                label,
                normalizar(btn.dataset[`actual${capitalize(key)}`]),
                normalizar(btn.dataset[`nuevo${capitalize(key)}`])
            );
        });

        return;
    }
    function addDiffRow(tbody, campo, actual, nuevo) {
        const tr = document.createElement('tr');

        const distintos = actual !== nuevo;

        tr.innerHTML = `
        <td>${campo}</td>
        <td class="${distintos ? 'text-muted' : ''}">${actual}</td>
        <td class="${distintos ? 'fw-bold text-danger' : ''}">${nuevo}</td>
    `;

        if (distintos) {
            tr.classList.add('table-warning');
        }

        tbody.appendChild(tr);
    }


    function capitalize(str) {
        return str.charAt(0).toUpperCase() + str.slice(1);
    }


    // ELIMINACION
    title.textContent = 'Detalle de eliminación';
    modoEli.classList.remove('d-none');

    document.getElementById('del_titulo').textContent = btn.dataset.actualTitulo ?? '-';
    document.getElementById('del_motivo').textContent = btn.dataset.motivo ?? '-';

    const spam = btn.dataset.esspam;
    document.getElementById('del_spam').textContent = (spam === 'true') ? 'Sí' : 'No';
});
