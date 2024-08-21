document.getElementById('calculateButton').addEventListener('click', () => {
    try {
        // Retrieve input values from the DOM
        const def = parseInt(document.getElementById('def').value);
        const leadSkill = parseInt(document.getElementById('leadSkill').value);
        const defPass = parseInt(document.getElementById('defPass').value) + parseInt(document.getElementById('defSupport').value);
        const defPLinks = parseInt(document.getElementById('defPLinks').value);
        const defFLinks = parseInt(document.getElementById('defFLinks').value);
        const buDefPass = parseInt(document.getElementById('buDefPass').value);
        const attackDefense = parseInt(document.getElementById('attackDefense').value);
        const saDefense = parseInt(document.getElementById('saDefense').value);
        const saDefense2 = parseInt(document.getElementById('saDefense2').value);
        const saTimes = parseInt(document.getElementById('saTimes').value);

        if (saTimes === 0) {
            alert("Number of Supers per Turn cannot be 0");
            return;
        }

        // Perform calculations
        const sotDef = Math.floor(def * (1 + leadSkill / 100)) + defPass + defFLinks;
        const fullBuiltDef = Math.floor(sotDef * (1 + defPLinks / 100)) + buDefPass;
        let maxDef = Math.floor(fullBuiltDef * (1 + attackDefense / 100));

        const superDefs = [];
        for (let i = 1; i <= saTimes; i++) {
            if (i === 1) {
                maxDef = Math.floor(maxDef * (1 + saDefense / 100));
            } else if (i === 2 && saDefense2 > 0) {
                maxDef = Math.floor(maxDef * (1 + saDefense2 / 100));
            } else {
                maxDef = Math.floor(maxDef * (1 + saDefense / 100));
            }
            superDefs.push(maxDef);
        }

        // Display results in the DOM
        document.getElementById('sotDefLabel').innerText = "SoT Defense: " + sotDef;
        document.getElementById('fullBuiltDefLabel').innerText = "Fully Built-up SoT Defense: " + fullBuiltDef;
        document.getElementById('maxDefLabel').innerText = "Max Possible Defense: " + maxDef;

        const superDefPanel = document.getElementById('superDefPanel');
        superDefPanel.innerHTML = '';
        superDefs.forEach((def, index) => {
            const p = document.createElement('p');
            p.innerText = `Defense after ${index + 1} Super(s): ${def}`;
            superDefPanel.appendChild(p);
        });

    } catch (error) {
        alert("An error occurred: " + error.message);
    }
});
