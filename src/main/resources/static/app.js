// ===== State =====
let currentLocale = 'sv';

const i18n = {
    sv: {
        'nav.schedules': 'Scheman',
        'nav.shifts': 'Skiftinstanser',
        'nav.workingTime': 'Arbetstid',
        'nav.holidays': 'Helgdagar',
        'schedules.title': 'Hantera scheman',
        'schedules.create': 'Skapa schema',
        'schedules.refresh': 'Uppdatera',
        'schedules.empty': 'Inga scheman skapade. Klicka "Skapa schema" eller ladda exempelschemat.',
        'schedules.quickStart': 'Snabbstart',
        'schedules.quickStartDesc': 'Ladda ett exempel p\u00e5 tillverkningsschema med 3 skift och 3 lag:',
        'schedules.loadSample': 'Ladda exempelschema',
        'shifts.title': 'Skiftinstanser f\u00f6r datum',
        'shifts.schedule': 'Schema:',
        'shifts.date': 'Datum:',
        'shifts.lookup': 'S\u00f6k',
        'shifts.team': 'Lag',
        'shifts.shift': 'Skift',
        'shifts.start': 'Start',
        'shifts.end': 'Slut',
        'shifts.noShifts': 'Inga skift denna dag.',
        'workingTime.title': 'Ber\u00e4kna arbetstid',
        'workingTime.schedule': 'Schema:',
        'workingTime.from': 'Fr\u00e5n:',
        'workingTime.to': 'Till:',
        'workingTime.calculate': 'Ber\u00e4kna',
        'workingTime.working': 'Arbetstid',
        'workingTime.nonWorking': 'Icke-arbetstid',
        'workingTime.period': 'Period',
        'holidays.title': 'Svenska helgdagar',
        'holidays.schedule': 'Schema:',
        'holidays.year': '\u00c5r:',
        'holidays.add': 'L\u00e4gg till helgdagar',
        'holidays.added': 'helgdagar tillagda!',
        'modal.title': 'Skapa nytt schema',
        'modal.name': 'Namn:',
        'modal.description': 'Beskrivning:',
        'modal.shifts': 'Skift',
        'modal.addShift': 'L\u00e4gg till skift',
        'modal.rotations': 'Rotationer',
        'modal.addRotation': 'L\u00e4gg till rotation',
        'modal.teams': 'Lag',
        'modal.addTeam': 'L\u00e4gg till lag',
        'modal.cancel': 'Avbryt',
        'modal.save': 'Spara',
        'toast.created': 'Schema skapat!',
        'toast.deleted': 'Schema borttaget!',
        'toast.error': 'Fel',
        'detail.shifts': 'skift',
        'detail.teams': 'lag',
        'detail.holidays': 'helgdagar',
        'detail.delete': 'Ta bort',
        'detail.view': 'Visa'
    },
    en: {
        'nav.schedules': 'Schedules',
        'nav.shifts': 'Shift Instances',
        'nav.workingTime': 'Working Time',
        'nav.holidays': 'Holidays',
        'schedules.title': 'Manage Schedules',
        'schedules.create': 'Create Schedule',
        'schedules.refresh': 'Refresh',
        'schedules.empty': 'No schedules created. Click "Create Schedule" or load the sample.',
        'schedules.quickStart': 'Quick Start',
        'schedules.quickStartDesc': 'Load a sample manufacturing schedule with 3 shifts and 3 teams:',
        'schedules.loadSample': 'Load Sample Schedule',
        'shifts.title': 'Shift Instances for Date',
        'shifts.schedule': 'Schedule:',
        'shifts.date': 'Date:',
        'shifts.lookup': 'Lookup',
        'shifts.team': 'Team',
        'shifts.shift': 'Shift',
        'shifts.start': 'Start',
        'shifts.end': 'End',
        'shifts.noShifts': 'No shifts on this day.',
        'workingTime.title': 'Calculate Working Time',
        'workingTime.schedule': 'Schedule:',
        'workingTime.from': 'From:',
        'workingTime.to': 'To:',
        'workingTime.calculate': 'Calculate',
        'workingTime.working': 'Working Time',
        'workingTime.nonWorking': 'Non-Working Time',
        'workingTime.period': 'Period',
        'holidays.title': 'Swedish Holidays',
        'holidays.schedule': 'Schedule:',
        'holidays.year': 'Year:',
        'holidays.add': 'Add Holidays',
        'holidays.added': 'holidays added!',
        'modal.title': 'Create New Schedule',
        'modal.name': 'Name:',
        'modal.description': 'Description:',
        'modal.shifts': 'Shifts',
        'modal.addShift': 'Add Shift',
        'modal.rotations': 'Rotations',
        'modal.addRotation': 'Add Rotation',
        'modal.teams': 'Teams',
        'modal.addTeam': 'Add Team',
        'modal.cancel': 'Cancel',
        'modal.save': 'Save',
        'toast.created': 'Schedule created!',
        'toast.deleted': 'Schedule deleted!',
        'toast.error': 'Error',
        'detail.shifts': 'shifts',
        'detail.teams': 'teams',
        'detail.holidays': 'holidays',
        'detail.delete': 'Delete',
        'detail.view': 'View'
    }
};

function t(key) {
    return i18n[currentLocale][key] || key;
}

// ===== API =====
const API = '/api/schedules';

async function api(path, options = {}) {
    const headers = { 'Accept-Language': currentLocale, ...options.headers };
    if (options.body) headers['Content-Type'] = 'application/json';

    const resp = await fetch(API + path, { ...options, headers });
    if (!resp.ok) {
        const err = await resp.json().catch(() => ({ message: resp.statusText }));
        throw new Error(err.message || 'Request failed');
    }
    if (resp.status === 204) return null;
    return resp.json();
}

// ===== Locale =====
function setLocale(lang) {
    currentLocale = lang;
    document.querySelectorAll('.locale-btn').forEach(btn => btn.classList.remove('active'));
    document.getElementById('btn-' + lang).classList.add('active');
    document.documentElement.lang = lang;
    applyTranslations();
    loadSchedules();
}

function applyTranslations() {
    document.querySelectorAll('[data-i18n]').forEach(el => {
        const key = el.getAttribute('data-i18n');
        if (i18n[currentLocale][key]) {
            el.textContent = i18n[currentLocale][key];
        }
    });
}

// ===== Tabs =====
function showTab(tabId) {
    document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(t => t.classList.remove('active'));
    document.querySelector(`[data-tab="${tabId}"]`).classList.add('active');
    document.getElementById('tab-' + tabId).classList.add('active');
    populateSelects();
}

// ===== Schedules =====
async function loadSchedules() {
    try {
        const schedules = await api('');
        const list = document.getElementById('schedules-list');

        if (!schedules.length) {
            list.innerHTML = `<p class="empty-state">${t('schedules.empty')}</p>`;
            return;
        }

        list.innerHTML = schedules.map(s => `
            <div class="schedule-item">
                <div class="schedule-info">
                    <h3>${esc(s.name)}</h3>
                    <p>${esc(s.description || '')}</p>
                    <div class="schedule-meta">
                        <span class="meta-badge">${s.shifts.length} ${t('detail.shifts')}</span>
                        <span class="meta-badge">${s.teams.length} ${t('detail.teams')}</span>
                        <span class="meta-badge">${s.rotations.length} rotationer</span>
                        ${s.nonWorkingPeriods.length ? `<span class="meta-badge holiday">${s.nonWorkingPeriods.length} ${t('detail.holidays')}</span>` : ''}
                    </div>
                </div>
                <div class="schedule-actions">
                    <button class="btn btn-danger btn-small" onclick="deleteSchedule('${esc(s.name)}')">${t('detail.delete')}</button>
                </div>
            </div>
        `).join('');

        populateSelects();
    } catch (e) {
        showToast(e.message, 'error');
    }
}

async function deleteSchedule(name) {
    try {
        await api('/' + encodeURIComponent(name), { method: 'DELETE' });
        showToast(t('toast.deleted'), 'success');
        loadSchedules();
    } catch (e) {
        showToast(e.message, 'error');
    }
}

async function createSampleSchedule() {
    const monday = getNextMonday();
    const sample = {
        name: 'Tillverkning',
        description: '24/7 tillverkningsschema med 3 skift',
        shifts: [
            { name: 'Dag', description: 'Dagskift 07-15', start: '07:00:00', duration: 'PT8H', breaks: [] },
            { name: 'Kv\u00e4ll', description: 'Kv\u00e4llsskift 15-23', start: '15:00:00', duration: 'PT8H', breaks: [] },
            { name: 'Natt', description: 'Nattskift 23-07', start: '23:00:00', duration: 'PT8H', breaks: [] }
        ],
        rotations: [
            { name: 'DagRot', description: 'Dagrotation', segments: [{ shiftName: 'Dag', daysOn: 5, daysOff: 2 }] },
            { name: 'Kv\u00e4llRot', description: 'Kv\u00e4llsrotation', segments: [{ shiftName: 'Kv\u00e4ll', daysOn: 5, daysOff: 2 }] },
            { name: 'NattRot', description: 'Nattrotation', segments: [{ shiftName: 'Natt', daysOn: 5, daysOff: 2 }] }
        ],
        teams: [
            { name: 'Lag A', description: 'Dagteam', rotationName: 'DagRot', rotationStart: monday, members: [] },
            { name: 'Lag B', description: 'Kv\u00e4llsteam', rotationName: 'Kv\u00e4llRot', rotationStart: monday, members: [] },
            { name: 'Lag C', description: 'Nattteam', rotationName: 'NattRot', rotationStart: monday, members: [] }
        ],
        nonWorkingPeriods: []
    };

    try {
        await api('', { method: 'POST', body: JSON.stringify(sample) });
        showToast(t('toast.created'), 'success');
        loadSchedules();
    } catch (e) {
        showToast(e.message, 'error');
    }
}

// ===== Create Schedule Modal =====
function openCreateModal() {
    document.getElementById('create-modal').classList.add('open');
    // Set default date
    document.querySelectorAll('.team-start').forEach(el => {
        if (!el.value) el.value = getNextMonday();
    });
}

function closeModal() {
    document.getElementById('create-modal').classList.remove('open');
}

function closeModalOutside(event) {
    if (event.target === document.getElementById('create-modal')) closeModal();
}

function addShiftRow() {
    const container = document.getElementById('shifts-container');
    const row = document.createElement('div');
    row.className = 'shift-row';
    row.innerHTML = `
        <input type="text" class="input shift-name" placeholder="Namn" required>
        <input type="time" class="input shift-start" required>
        <input type="text" class="input shift-duration" placeholder="PT8H" value="PT8H" required>
        <button type="button" class="btn-icon remove-shift" onclick="removeShiftRow(this)">&times;</button>
    `;
    container.appendChild(row);
}

function removeShiftRow(btn) {
    const container = document.getElementById('shifts-container');
    if (container.children.length > 1) btn.parentElement.remove();
}

function addRotationRow() {
    const container = document.getElementById('rotations-container');
    const row = document.createElement('div');
    row.className = 'rotation-row';
    row.innerHTML = `
        <input type="text" class="input rot-name" placeholder="Rotationsnamn" required>
        <input type="text" class="input rot-shift" placeholder="Skiftnamn" required>
        <input type="number" class="input rot-on" placeholder="Dagar p\u00e5" value="5" min="1" required>
        <input type="number" class="input rot-off" placeholder="Dagar av" value="2" min="0" required>
    `;
    container.appendChild(row);
}

function addTeamRow() {
    const container = document.getElementById('teams-container');
    const row = document.createElement('div');
    row.className = 'team-row';
    row.innerHTML = `
        <input type="text" class="input team-name" placeholder="Lagnamn" required>
        <input type="text" class="input team-rotation" placeholder="Rotationsnamn" required>
        <input type="date" class="input team-start" value="${getNextMonday()}" required>
    `;
    container.appendChild(row);
}

async function createSchedule(event) {
    event.preventDefault();

    const name = document.getElementById('sched-name').value;
    const desc = document.getElementById('sched-desc').value;

    const shifts = Array.from(document.querySelectorAll('.shift-row')).map(row => ({
        name: row.querySelector('.shift-name').value,
        description: row.querySelector('.shift-name').value,
        start: row.querySelector('.shift-start').value + ':00',
        duration: row.querySelector('.shift-duration').value,
        breaks: []
    }));

    const rotations = Array.from(document.querySelectorAll('.rotation-row')).map(row => ({
        name: row.querySelector('.rot-name').value,
        description: row.querySelector('.rot-name').value,
        segments: [{
            shiftName: row.querySelector('.rot-shift').value,
            daysOn: parseInt(row.querySelector('.rot-on').value),
            daysOff: parseInt(row.querySelector('.rot-off').value)
        }]
    }));

    const teams = Array.from(document.querySelectorAll('.team-row')).map(row => ({
        name: row.querySelector('.team-name').value,
        description: row.querySelector('.team-name').value,
        rotationName: row.querySelector('.team-rotation').value,
        rotationStart: row.querySelector('.team-start').value,
        members: []
    }));

    const schedule = {
        name, description: desc || name, shifts, rotations, teams, nonWorkingPeriods: []
    };

    try {
        await api('', { method: 'POST', body: JSON.stringify(schedule) });
        showToast(t('toast.created'), 'success');
        closeModal();
        loadSchedules();
    } catch (e) {
        showToast(e.message, 'error');
    }
}

// ===== Shift Instances =====
async function lookupShifts(event) {
    event.preventDefault();
    const name = document.getElementById('shift-schedule-select').value;
    const date = document.getElementById('shift-date').value;
    if (!name || !date) return;

    try {
        const instances = await api('/' + encodeURIComponent(name) + '/shifts/' + date);
        const results = document.getElementById('shift-results');

        if (!instances.length) {
            results.innerHTML = `<p class="empty-state">${t('shifts.noShifts')}</p>`;
            return;
        }

        results.innerHTML = `
            <table class="results-table">
                <thead>
                    <tr>
                        <th>${t('shifts.team')}</th>
                        <th>${t('shifts.shift')}</th>
                        <th>${t('shifts.start')}</th>
                        <th>${t('shifts.end')}</th>
                    </tr>
                </thead>
                <tbody>
                    ${instances.map(si => `
                        <tr>
                            <td>${esc(si.teamName)}</td>
                            <td>${esc(si.shiftName)}</td>
                            <td>${formatDateTime(si.startTime)}</td>
                            <td>${formatDateTime(si.endTime)}</td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        `;
    } catch (e) {
        showToast(e.message, 'error');
    }
}

// ===== Working Time =====
async function calcWorkingTime(event) {
    event.preventDefault();
    const name = document.getElementById('wt-schedule-select').value;
    const from = document.getElementById('wt-from').value;
    const to = document.getElementById('wt-to').value;
    if (!name || !from || !to) return;

    try {
        const result = await api('/' + encodeURIComponent(name) + '/working-time?from=' + from + ':00&to=' + to + ':00');
        const results = document.getElementById('wt-results');

        results.innerHTML = `
            <div class="result-summary">
                <div class="summary-item">
                    <label>${t('workingTime.period')}</label>
                    <div class="value" style="font-size:1rem">${formatDateTime(result.from)} &rarr; ${formatDateTime(result.to)}</div>
                </div>
                <div class="summary-item">
                    <label>${t('workingTime.working')}</label>
                    <div class="value">${formatDuration(result.workingTime)}</div>
                </div>
                <div class="summary-item">
                    <label>${t('workingTime.nonWorking')}</label>
                    <div class="value">${formatDuration(result.nonWorkingTime)}</div>
                </div>
            </div>
        `;
    } catch (e) {
        showToast(e.message, 'error');
    }
}

// ===== Holidays =====
async function addHolidays(event) {
    event.preventDefault();
    const name = document.getElementById('hol-schedule-select').value;
    const year = document.getElementById('hol-year').value;
    if (!name || !year) return;

    try {
        const result = await api('/' + encodeURIComponent(name) + '/holidays/swedish/' + year, { method: 'POST' });
        const results = document.getElementById('hol-results');
        const holidays = result.nonWorkingPeriods;

        results.innerHTML = `
            <p style="margin-bottom:0.75rem; color: var(--success); font-weight:500">
                ${holidays.length} ${t('holidays.added')}
            </p>
            <div class="holiday-list">
                ${holidays.map(h => `
                    <div class="holiday-item">
                        <span class="holiday-name">${esc(h.name)}</span>
                        <span class="holiday-date">${formatDate(h.startDateTime)}</span>
                    </div>
                `).join('')}
            </div>
        `;
        loadSchedules();
    } catch (e) {
        showToast(e.message, 'error');
    }
}

// ===== Helpers =====
function populateSelects() {
    api('').then(schedules => {
        const selects = ['shift-schedule-select', 'wt-schedule-select', 'hol-schedule-select'];
        selects.forEach(id => {
            const select = document.getElementById(id);
            if (!select) return;
            const current = select.value;
            select.innerHTML = schedules.map(s =>
                `<option value="${esc(s.name)}" ${s.name === current ? 'selected' : ''}>${esc(s.name)}</option>`
            ).join('');
            if (!schedules.length) {
                select.innerHTML = '<option value="">--</option>';
            }
        });
    }).catch(() => {});
}

function showToast(msg, type) {
    const toast = document.getElementById('toast');
    toast.textContent = msg;
    toast.className = 'toast ' + type + ' show';
    setTimeout(() => toast.classList.remove('show'), 3000);
}

function esc(str) {
    const div = document.createElement('div');
    div.textContent = str;
    return div.innerHTML;
}

function formatDateTime(dt) {
    if (!dt) return '';
    return dt.replace('T', ' ').substring(0, 16);
}

function formatDate(dt) {
    if (!dt) return '';
    return dt.substring(0, 10);
}

function formatDuration(iso) {
    if (!iso) return '0h';
    // Parse PT...H...M...S
    const match = iso.match(/PT(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)S)?/);
    if (!match) return iso;
    const h = match[1] || 0;
    const m = match[2] || 0;
    const s = match[3] || 0;
    let result = '';
    if (h > 0) result += h + 'h ';
    if (m > 0) result += m + 'm ';
    if (h == 0 && m == 0) result = s + 's';
    return result.trim() || '0h';
}

function getNextMonday() {
    const d = new Date();
    const day = d.getDay();
    const diff = day === 0 ? 1 : (day === 1 ? 0 : 8 - day);
    d.setDate(d.getDate() + diff);
    return d.toISOString().split('T')[0];
}

// ===== Init =====
document.addEventListener('DOMContentLoaded', () => {
    // Set today's date as default
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('shift-date').value = today;
    document.getElementById('wt-from').value = today + 'T07:00';
    document.getElementById('wt-to').value = today + 'T15:00';

    loadSchedules();
});
