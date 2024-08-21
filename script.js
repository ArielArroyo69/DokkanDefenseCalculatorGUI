function calculateDefense() {
    try {
        // Parse input fields
        const def = parseInt(document.getElementById('def').value);
        const leadSkill = parseInt(document.getElementById('leadSkill').value);
        const defPass = parseInt(document.getElementById('defPass').value) + parseInt(document.getElementById('defSupport').value);
        const defPLinks = parseInt(document.getElementById('defPLinks').value);
        const defFLinks = parseInt(document.getElementById('defFLinks').value);
        const buDefPass = parseInt(document.getElementById('buDefPass').value);
        const attackDefense = parseInt(document.getElementById('attackDefense').value);
        const saDefense = parseInt(document.getElementById('saDefense').value);
        const saDefense2 = parseInt(document.getElementById('saDefense2').value);
        let saTimes = parseInt(document.getElementById('saTimes').value);
        if (saTimes === 0) saTimes = 1;

        // Calculate defenses
        const sotDef = calculateSoTDefense(def, leadSkill, defPass, defPLinks, defFLinks);
        const fullBuiltUpDef = calculateFullBuiltUpSoTDefense(sotDef, buDefPass);
        const maxDef = calculateMaxDefense(sotDef, buDefPass, attackDefense, saDefense, saDefense2, saTimes);

        // Calculate defense after each super
        const defenseAfterSupers = calculateDefenseAfterEachSuper(sotDef, buDefPass, attackDefense, saDefense, saDefense2, saTimes);

        // Display the results
        document.getElementById('sotDefLabel').innerText = "SoT Defense: " + sotDef;
        document.getElementById('fullBuiltDefLabel').innerText = "Fully Built-up SoT Defense: " + fullBuiltUpDef;
        document.getElementById('maxDefLabel').innerText = "Max Possible Defense: " + maxDef;

        // Clear previous labels
        const superDefPanel = document.getElementById('superDefPanel');
        superDefPanel.innerHTML = '';

        // Add new labels based on the number of supers
        for (let i = 0; i < saTimes; i++) {
            const superDefLabel = document.createElement('p');
            superDefLabel.innerText = "Defense after " + (i + 1) + " Super(s): " + defenseAfterSupers[i];
            superDefPanel.appendChild(superDefLabel);
        }

    } catch (error) {
        alert("Please enter valid numbers!");
    }
}

// Calculate SoT Defense
function calculateSoTDefense(def, leadSkill, defPass, defPLinks, defFLinks) {
    let sotDef = def;
    sotDef *= (leadSkill + 100) / 100.0;
    sotDef *= (defPass + 100) / 100.0;
    sotDef *= (defPLinks + 100) / 100.0;
    sotDef += defFLinks;
    return sotDef;
}

// Calculate Fully Built-up SoT Defense
function calculateFullBuiltUpSoTDefense(sotDef, buDefPass) {
    return sotDef * (buDefPass + 100) / 100.0;
}

// Calculate Max Defense after supering
function calculateMaxDefense(sotDef, buDefPass, attackDefense, saDefense, saDefense2, satimesperTurn) {
    let maxDef = calculateFullBuiltUpSoTDefense(sotDef, buDefPass);
    maxDef *= (attackDefense + 100) / 100.0;

    if (satimesperTurn === 1) {
        maxDef *= (saDefense + 100) / 100.0;
    } else if (saDefense2 > 0) {
        maxDef *= (saDefense + (saDefense2 * (satimesperTurn - 1)) + 100) / 100.0;
    } else {
        maxDef *= (saDefense * satimesperTurn + 100) / 100.0;
    }
    return maxDef;
}

// Calculate defense after each super
function calculateDefenseAfterEachSuper(sotDef, buDefPass, attackDefense, saDefense, saDefense2, satimesperTurn) {
    let currentDef = calculateFullBuiltUpSoTDefense(sotDef, buDefPass);
    currentDef *= (attackDefense + 100) / 100.0;
    const staticDef = currentDef;

    const defenseAfterSupers = [];
    for (let i = 0; i < satimesperTurn; i++) {
        if (i === 0) {
            currentDef = staticDef * (saDefense + 100) / 100.0;
        } else if (saDefense2 > 0) {
            currentDef = staticDef * (saDefense + (saDefense2 * i) + 100) / 100.0;
        } else {
            currentDef = staticDef * (saDefense * (i + 1) + 100) / 100.0;
        }
        defenseAfterSupers.push(currentDef);
    }
    return defenseAfterSupers;
}
