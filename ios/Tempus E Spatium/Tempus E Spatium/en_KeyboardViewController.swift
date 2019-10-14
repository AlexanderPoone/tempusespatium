//
//  en_KeyboardViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 9/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit

class en_KeyboardViewController: UIViewController {

    @IBAction func mKeyPressed(_ sender: UIButton) {
        sender.title(for: .normal)
    }
    
    @IBAction func mBackspace() {
    }
    
    
    @IBAction func mDelete() {
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        if let foundView = view.viewWithTag(0xDEADBEEF) {
            foundView.removeFromSuperview()
        }
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
